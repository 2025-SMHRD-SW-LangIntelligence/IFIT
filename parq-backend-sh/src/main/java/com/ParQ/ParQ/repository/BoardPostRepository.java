package com.ParQ.ParQ.repository;

import com.ParQ.ParQ.entity.BoardPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {
} 