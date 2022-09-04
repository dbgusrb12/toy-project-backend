package com.hg.blog.domain.comment.service;

import com.hg.blog.domain.comment.entity.Comment;
import com.hg.blog.domain.comment.entity.CommentRepository;
import com.hg.blog.domain.dto.DefaultPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentQueryService {

    private final CommentRepository commentRepository;

    public Comment getComment(long commentId) {
        return commentRepository.findByIdAndDeleted(commentId, false)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
    }

    public DefaultPage<Comment> getComments(long postId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "created"));
        return DefaultPage.of(commentRepository.findByPost_IdAndParentCommentIsNullAndDeleted(postId, false, pageable));
    }

    public DefaultPage<Comment> getChildComments(long commentId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "created"));
        return DefaultPage.of(commentRepository.findByParentComment_IdAndDeleted(commentId, false, pageable));
    }

}
