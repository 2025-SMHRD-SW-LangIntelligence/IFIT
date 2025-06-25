package com.ParQ.ParQ.dto;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import com.ParQ.ParQ.entity.BoardPost;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class BoardPostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String createdAt;
    private AuthorDto author;
    private boolean hasReply;
    private String fileUrls;

    public BoardPostResponseDto(BoardPost post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt() != null ? post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
        this.author = new AuthorDto(
            post.getAuthor().getId(),
            post.getAuthor().getUsername(),
            post.getAuthor().getEmail()
        );
        this.hasReply = post.getReplies() != null && !post.getReplies().isEmpty();
        this.fileUrls = post.getFileUrls();
    }

    public List<String> getFileUrls() {
        if (fileUrls == null || fileUrls.isEmpty()) return new ArrayList<>();
        return Arrays.asList(fileUrls.split(","));
    }

    @Getter
    @AllArgsConstructor
    public static class AuthorDto {
        private Long id;
        private String username;
        private String email;
    }
} 