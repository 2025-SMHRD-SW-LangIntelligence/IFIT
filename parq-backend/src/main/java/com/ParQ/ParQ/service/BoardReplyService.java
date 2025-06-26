
package com.ParQ.ParQ.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ParQ.ParQ.dto.BoardReplyRequestDto;
import com.ParQ.ParQ.dto.BoardReplyResponseDto;
import com.ParQ.ParQ.entity.BoardPost;
import com.ParQ.ParQ.entity.BoardReply;
import com.ParQ.ParQ.entity.User;
import com.ParQ.ParQ.repository.BoardPostRepository;
import com.ParQ.ParQ.repository.BoardReplyRepository;
import com.ParQ.ParQ.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardReplyService {
    private final BoardReplyRepository boardReplyRepository;
    private final BoardPostRepository boardPostRepository;
    private final UserRepository userRepository;

    // 답변 등록 (관리자만)
    public BoardReplyResponseDto createReply(BoardReplyRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElse(null);
//        if (user == null || !"ADMIN".equals(user.getRole())) {
//            throw new IllegalArgumentException("관리자만 답변을 작성할 수 있습니다.");
//        }
        BoardPost post = boardPostRepository.findById(requestDto.getPostId()).orElse(null);
        if (post == null) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다.");
        }
        BoardReply reply = new BoardReply();
        reply.setContent(requestDto.getContent());
        reply.setAuthor(user);
        reply.setPost(post);
        BoardReply saved = boardReplyRepository.save(reply);
        return new BoardReplyResponseDto(saved);
    }

    // 게시글별 답변 조회 (1개만 있다고 가정)
    public BoardReplyResponseDto getReplyByPostId(Long postId) {
        BoardReply reply = boardReplyRepository.findAll().stream()
        	.filter(r -> r.getPost().getId().equals(postId))
            .findFirst().orElse(null);
        return reply != null ? new BoardReplyResponseDto(reply) : null;
    }

    // (확장) 모든 답변 조회
    public List<BoardReplyResponseDto> getAllReplies() {
        return boardReplyRepository.findAll().stream()
            .map(BoardReplyResponseDto::new)
            .collect(Collectors.toList());
    }
} 
