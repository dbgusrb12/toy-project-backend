package com.hg.blog.domain.comment.service;

import com.hg.blog.domain.comment.entity.Comment;
import com.hg.blog.domain.comment.entity.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentQueryService {

    private final CommentRepository commentRepository;

    public Comment getComment(long commentId) {
        return commentRepository.findByIdAndDeleted(commentId, false)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
    }
}
