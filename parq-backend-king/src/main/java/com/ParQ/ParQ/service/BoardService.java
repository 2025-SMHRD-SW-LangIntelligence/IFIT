package com.ParQ.ParQ.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ParQ.ParQ.dto.BoardPostRequestDto;
import com.ParQ.ParQ.dto.BoardPostResponseDto;
import com.ParQ.ParQ.entity.BoardPost;
import com.ParQ.ParQ.entity.User;
import com.ParQ.ParQ.repository.BoardPostRepository;
import com.ParQ.ParQ.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardPostRepository boardPostRepository;
    private final UserRepository userRepository;

    // 게시글 작성
    public BoardPostResponseDto createPost(BoardPostRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElse(null);

        if (user == null) {
            return null;
        }

        BoardPost post = new BoardPost();
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setAuthor(user);

        BoardPost savedPost = boardPostRepository.save(post);
        return new BoardPostResponseDto(savedPost);
    }

    // 모든 게시글 조회
    public List<BoardPostResponseDto> getAllPosts() {
        return boardPostRepository.findAll().stream()
                .map(BoardPostResponseDto::new)
                .collect(Collectors.toList());
    }

    // 게시글 삭제
    public void deletePost(Long id) {
        boardPostRepository.deleteById(id);
    }

    // 게시글 수정
    public BoardPostResponseDto updatePost(Long id, BoardPostRequestDto requestDto) {
        BoardPost post = boardPostRepository.findById(id).orElse(null);
        if (post == null) {
            return null;
        }
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        BoardPost saved = boardPostRepository.save(post);
        return new BoardPostResponseDto(saved);
    }

    // 특정 사용자가 작성한 게시글 목록 조회
    public List<BoardPostResponseDto> getPostsByUser(Long userId) {
        return boardPostRepository.findAll().stream()
            .filter(post -> post.getAuthor().getId().equals(userId))
            .map(BoardPostResponseDto::new)
            .collect(Collectors.toList());
    }

    public BoardPostResponseDto createPostWithFiles(String title, String content, Long userId, List<MultipartFile> files) {
        System.out.println("SERVER DIR: " + System.getProperty("user.dir"));
        System.out.println("files: " + files);
        if (files != null) {
            System.out.println("files.size: " + files.size());
            for (MultipartFile file : files) {
                System.out.println("file name: " + file.getOriginalFilename() + ", isEmpty: " + file.isEmpty());
            }
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;

        BoardPost post = new BoardPost();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(user);

        List<String> fileUrlList = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String originalName = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.]", "");
                    String fileName = UUID.randomUUID() + "_" + originalName;
                    String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
                    File dir = new File(uploadDir);
                    if (!dir.exists()) dir.mkdirs();
                    String savePath = uploadDir + fileName;
                    try {
                        file.transferTo(new File(savePath));
                        fileUrlList.add("/uploads/" + fileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        String fileUrls = String.join(",", fileUrlList);
        post.setFileUrls(fileUrls);
        BoardPost savedPost = boardPostRepository.save(post);
        return new BoardPostResponseDto(savedPost);
    }
} 