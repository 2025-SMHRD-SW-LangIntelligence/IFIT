package com.ParQ.ParQ.dto;

import com.ParQ.ParQ.entity.BoardPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class BoardPostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String createdAt;
    private AuthorDto author;
    private boolean hasReply;

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
    }

    @Getter
    @AllArgsConstructor
    public static class AuthorDto {
        private Long id;
        private String username;
        private String email;
    }
} 