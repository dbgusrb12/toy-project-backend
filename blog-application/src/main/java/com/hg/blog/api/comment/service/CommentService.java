package com.hg.blog.api.comment.service;

import com.hg.blog.api.comment.dto.CommentDto.ChildCommentCreateCommand;
import com.hg.blog.api.comment.dto.CommentDto.CommentCreateCommand;
import com.hg.blog.api.comment.dto.CommentDto.CommentUpdateCommand;
import com.hg.blog.api.comment.dto.CommentDto.GetComment;
import com.hg.blog.api.comment.dto.CommentType;
import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.service.AccountQueryService;
import com.hg.blog.domain.comment.entity.Comment;
import com.hg.blog.domain.comment.service.CommentCommandService;
import com.hg.blog.domain.comment.service.CommentQueryService;
import com.hg.blog.domain.dto.DefaultPage;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.service.PostQueryService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;
    private final AccountQueryService accountQueryService;
    private final PostQueryService postQueryService;

    @Transactional
    public void saveComment(String userId, CommentCreateCommand command) {
        final Account owner = accountQueryService.getAccountByUserId(userId);
        final Post post = postQueryService.getPost(command.getPostId());
        commentCommandService.saveComment(owner, post, command.getContent());
    }

    @Transactional
    public void updateComment(long commentId, String userId, CommentUpdateCommand command) {
        final Account owner = accountQueryService.getAccountByUserId(userId);
        commentCommandService.updateComment(owner, commentId, command.getContent());
    }

    @Transactional
    public void deleteComment(long commentId, String userId) {
        final Account owner = accountQueryService.getAccountByUserId(userId);
        commentCommandService.deleteComment(owner, commentId);
    }

    public GetComment getComment(long commentId) {
        Comment comment = commentQueryService.getComment(commentId);
        return GetComment.of(
            comment.getId(),
            comment.getPost().getId(),
            Optional.ofNullable(comment.getParentComment()).map(Comment::getId).orElse(null),
            comment.getContent(),
            comment.getAccount().getNickname(),
            comment.getCreated(),
            comment.getUpdated()
        );
    }

    @Transactional
    public void saveChildComment(long commentId, String userId, ChildCommentCreateCommand command) {
        final Account owner = accountQueryService.getAccountByUserId(userId);
        final Comment parentComment = commentQueryService.getComment(commentId);
        commentCommandService.saveChildComment(owner, parentComment, command.getContent());
    }


    public DefaultPage<GetComment> getComments(CommentType type, long refId, int page, int size) {
        DefaultPage<Comment> comments = type.isRoot()
            ? commentQueryService.getComments(refId, page, size)
            : commentQueryService.getChildComments(refId, page, size);
        return comments.map(comment ->
            GetComment.of(
                comment.getId(),
                comment.getPost().getId(),
                Optional.ofNullable(comment.getParentComment()).map(Comment::getId).orElse(null),
                comment.getContent(),
                comment.getAccount().getNickname(),
                comment.getCreated(),
                comment.getUpdated()
            ));
    }
}
