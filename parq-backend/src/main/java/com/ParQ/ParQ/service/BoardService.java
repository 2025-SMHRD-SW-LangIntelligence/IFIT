package com.ParQ.ParQ.service;

import com.ParQ.ParQ.dto.BoardPostRequestDto;
import com.ParQ.ParQ.dto.BoardPostResponseDto;
import com.ParQ.ParQ.entity.BoardPost;
import com.ParQ.ParQ.entity.User;
import com.ParQ.ParQ.repository.BoardPostRepository;
import com.ParQ.ParQ.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
} 