package com.hg.blog.api.comment.controller;

import static com.hg.blog.constants.Constants.API_PREFIX;
import static com.hg.blog.constants.Constants.COMMENT_API;

import com.hg.blog.annotation.Permit;
import com.hg.blog.annotation.Role;
import com.hg.blog.api.comment.dto.CommentDto;
import com.hg.blog.api.comment.service.CommentService;
import com.hg.blog.response.Response;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + COMMENT_API)
public class CommentController {

    private final CommentService commentService;

    @PostMapping("")
    @Permit(Role.USER)
    public Response<Long> saveComment(
        @RequestAttribute String userId,
        @Valid @RequestBody CommentDto.CommentCreateCommand command
    ) {
        return Response.of(commentService.saveComment(userId, command));
    }

    @PutMapping("/{commentId}")
    @Permit(Role.USER)
    public Response<Long> updateComment(
        @PathVariable long commentId,
        @RequestAttribute String userId,
        @Valid @RequestBody CommentDto.CommentUpdateCommand command
    ) {
        return Response.of(commentService.updateComment(commentId, userId, command));
    }
}
