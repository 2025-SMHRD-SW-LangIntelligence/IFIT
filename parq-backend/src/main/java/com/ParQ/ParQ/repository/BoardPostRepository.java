package com.ParQ.ParQ.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ParQ.ParQ.entity.BoardPost;

public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {
} 