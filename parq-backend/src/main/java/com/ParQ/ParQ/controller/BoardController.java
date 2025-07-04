package com.ParQ.ParQ.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ParQ.ParQ.dto.BoardPostRequestDto;
import com.ParQ.ParQ.dto.BoardPostResponseDto;
import com.ParQ.ParQ.dto.BoardReplyRequestDto;
import com.ParQ.ParQ.dto.BoardReplyResponseDto;
import com.ParQ.ParQ.service.BoardReplyService;
import com.ParQ.ParQ.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BoardController {

    private final BoardService boardService;
    private final BoardReplyService boardReplyService;

    // 게시글 작성
    @PostMapping("/posts")
    public ResponseEntity<BoardPostResponseDto> createPost(@RequestBody BoardPostRequestDto requestDto) {
        BoardPostResponseDto createdPost = boardService.createPost(requestDto);
        if (createdPost == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(createdPost);
    }

    // 모든 게시글 조회
    @GetMapping("/posts")
    public ResponseEntity<List<BoardPostResponseDto>> getAllPosts() {
        List<BoardPostResponseDto> posts = boardService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        boardService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // 게시글 수정
    @PutMapping("/posts/{id}")
    public ResponseEntity<BoardPostResponseDto> updatePost(@PathVariable Long id, @RequestBody BoardPostRequestDto requestDto) {
        BoardPostResponseDto updatedPost = boardService.updatePost(id, requestDto);
        if (updatedPost == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPost);
    }

    // 답변 등록 (관리자만)
    @PostMapping("/reply")
    public ResponseEntity<?> createReply(@RequestBody BoardReplyRequestDto requestDto) {
        try {
            BoardReplyResponseDto reply = boardReplyService.createReply(requestDto);
            return ResponseEntity.ok(reply);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    // 게시글별 답변 조회
    @GetMapping("/reply/{postId}")
    public ResponseEntity<BoardReplyResponseDto> getReplyByPostId(@PathVariable Long postId) {
        BoardReplyResponseDto reply = boardReplyService.getReplyByPostId(postId);
        if (reply == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reply);
    }

    // 특정 사용자가 작성한 게시글 목록 조회
    @GetMapping("/posts/user/{userId}")
    public ResponseEntity<List<BoardPostResponseDto>> getPostsByUser(@PathVariable Long userId) {
        List<BoardPostResponseDto> posts = boardService.getPostsByUser(userId);
        return ResponseEntity.ok(posts);
    }

    @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardPostResponseDto> createPostWithFiles(
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart("userId") String userId,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        Long userIdLong = Long.parseLong(userId);
        BoardPostResponseDto createdPost = boardService.createPostWithFiles(title, content, userIdLong, files);
        if (createdPost == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(createdPost);
    }
} 