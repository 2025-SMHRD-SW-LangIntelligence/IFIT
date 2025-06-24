package com.ParQ.ParQ.dto;

import lombok.Getter;

@Getter
public class BoardReplyRequestDto {
    private Long postId; // 답변을 달 게시글 ID
    private Long userId; // 답변 작성자(관리자) ID
    private String content; // 답변 내용
} 