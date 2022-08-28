package com.hg.blog.domain.comment.service;

import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.comment.entity.Comment;
import com.hg.blog.domain.comment.entity.CommentRepository;
import com.hg.blog.domain.post.entity.Post;
import java.security.AccessControlException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentCommandService {

    private final CommentRepository commentRepository;

    @Transactional
    public Comment saveComment(Account owner, Post post, String content) {
        Comment comment = Comment.of(owner, post, content);
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment saveComment(Account owner, Post post, Comment parentComment, String content) {
        Comment comment = Comment.of(owner, post, content);
        parentComment.addComment(comment);
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(Account owner, long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        checkUpdatePermission(owner, comment);
        comment.update(content);
        return commentRepository.save(comment);
    }

    private void checkUpdatePermission(Account account, Comment comment) {
        if (!account.equals(comment.getAccount())) {
            throw new AccessControlException("권한이 없습니다.");
        }
    }
}
