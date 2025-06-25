package com.ParQ.ParQ.dto;

import lombok.Getter;

@Getter
public class BoardPostRequestDto {
    private String title;
    private String content;
    private Long userId; // 작성자 ID
} 