package com.hg.blog.api.comment.controller;

import static com.hg.blog.constants.Constants.API_PREFIX;
import static com.hg.blog.constants.Constants.COMMENT_API;

import com.hg.blog.annotation.Permit;
import com.hg.blog.annotation.Role;
import com.hg.blog.api.comment.dto.CommentDto;
import com.hg.blog.api.comment.dto.CommentDto.GetComment;
import com.hg.blog.api.comment.dto.CommentType;
import com.hg.blog.api.comment.service.CommentService;
import com.hg.blog.domain.dto.DefaultPage;
import com.hg.blog.response.Response;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + COMMENT_API)
public class CommentController {

    private final CommentService commentService;

    @PostMapping("")
    @Permit(Role.USER)
    public Response<Void> saveComment(
        @RequestAttribute String userId,
        @Valid @RequestBody CommentDto.CommentCreateCommand command
    ) {
        commentService.saveComment(userId, command);
        return Response.ok();
    }

    @PutMapping("/{commentId}")
    @Permit(Role.USER)
    public Response<Void> updateComment(
        @PathVariable long commentId,
        @RequestAttribute String userId,
        @Valid @RequestBody CommentDto.CommentUpdateCommand command
    ) {
        commentService.updateComment(commentId, userId, command);
        return Response.ok();
    }

    @DeleteMapping("/{commentId}")
    @Permit(Role.USER)
    public Response<Void> deleteComment(@PathVariable long commentId, @RequestAttribute String userId) {
        commentService.deleteComment(commentId, userId);
        return Response.ok();
    }

    @GetMapping("/{commentId}")
    public Response<CommentDto.GetComment> getComment(@PathVariable long commentId) {
        return Response.of(commentService.getComment(commentId));
    }

    @PostMapping("/{commentId}")
    @Permit(Role.USER)
    public Response<Void> saveChildComment(
        @PathVariable long commentId,
        @RequestAttribute String userId,
        @Valid @RequestBody CommentDto.ChildCommentCreateCommand command
    ) {
        commentService.saveChildComment(commentId, userId, command);
        return Response.ok();
    }

    @GetMapping("")
    public Response<DefaultPage<GetComment>> getComments(
        @RequestParam long postId,
        @RequestParam int page,
        @RequestParam int size
    ) {
        return Response.of(commentService.getComments(CommentType.ROOT, postId, page, size));
    }

    @GetMapping("/{commentId}" + COMMENT_API)
    public Response<DefaultPage<GetComment>> getChildComments(
        @PathVariable long commentId,
        @RequestParam int page,
        @RequestParam int size
    ) {
        return Response.of(commentService.getComments(CommentType.CHILD, commentId, page, size));
    }
}
