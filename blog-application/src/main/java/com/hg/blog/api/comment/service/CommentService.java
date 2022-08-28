package com.hg.blog.api.comment.service;

import com.hg.blog.api.comment.dto.CommentDto.ChildCommentCreateCommand;
import com.hg.blog.api.comment.dto.CommentDto.CommentCreateCommand;
import com.hg.blog.api.comment.dto.CommentDto.CommentUpdateCommand;
import com.hg.blog.api.comment.dto.CommentDto.GetComment;
import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.service.AccountQueryService;
import com.hg.blog.domain.comment.entity.Comment;
import com.hg.blog.domain.comment.service.CommentCommandService;
import com.hg.blog.domain.comment.service.CommentQueryService;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.service.PostQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;
    private final AccountQueryService accountQueryService;
    private final PostQueryService postQueryService;

    public long saveComment(String userId, CommentCreateCommand command) {
        final Account owner = accountQueryService.getAccountByUserId(userId);
        final Post post = postQueryService.getPost(command.getPostId());
        final Comment comment = commentCommandService.saveComment(owner, post, command.getContent());
        return comment.getId();
    }

    public long updateComment(long commentId, String userId, CommentUpdateCommand command) {
        final Account owner = accountQueryService.getAccountByUserId(userId);
        final Comment comment = commentCommandService.updateComment(owner, commentId, command.getContent());
        return comment.getId();
    }

    public void deleteComment(long commentId, String userId) {
        final Account owner = accountQueryService.getAccountByUserId(userId);
        commentCommandService.deleteComment(owner, commentId);
    }

    public GetComment getComment(long commentId) {
        Comment comment = commentQueryService.getComment(commentId);
        return GetComment.of(
            comment.getId(),
            comment.getContent(),
            comment.getAccount().getNickname(),
            comment.getCreated(),
            comment.getUpdated()
        );
    }

    public long saveChildComment(long commentId, String userId, ChildCommentCreateCommand command) {
        final Account owner = accountQueryService.getAccountByUserId(userId);
        final Comment parentComment = commentQueryService.getComment(commentId);
        final Comment comment = commentCommandService.saveChildComment(owner, parentComment, command.getContent());
        return comment.getId();
    }
}
