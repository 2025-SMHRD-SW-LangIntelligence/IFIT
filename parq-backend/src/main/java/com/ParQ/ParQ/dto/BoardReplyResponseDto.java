package com.ParQ.ParQ.dto;

import com.ParQ.ParQ.entity.BoardReply;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.format.DateTimeFormatter;

@Getter
public class BoardReplyResponseDto {
    private Long id;
    private String content;
    private String createdAt;
    private AuthorDto author;

    public BoardReplyResponseDto(BoardReply reply) {
        this.id = reply.getId();
        this.content = reply.getContent();
        this.createdAt = reply.getCreatedAt() != null ? reply.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
        this.author = new AuthorDto(
            reply.getAuthor().getId(),
            reply.getAuthor().getUsername(),
            reply.getAuthor().getEmail()
        );
    }

    @Getter
    @AllArgsConstructor
    public static class AuthorDto {
        private Long id;
        private String username;
        private String email;
    }
} 