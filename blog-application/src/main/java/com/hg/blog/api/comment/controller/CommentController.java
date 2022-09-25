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
import com.hg.blog.security.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + COMMENT_API)
@Tag(name = "comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("")
    @Permit(Role.USER)
    @Operation(description = "댓글 생성")
    public Response<Void> saveComment(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @Valid @RequestBody CommentDto.CommentCreateCommand command
    ) {
        commentService.saveComment(user.getUsername(), command);
        return Response.OK;
    }

    @PutMapping("/{commentId}")
    @Permit(Role.USER)
    @Operation(description = "댓글 수정")
    public Response<Void> updateComment(
        @Parameter(description = "수정 할 comment id") @PathVariable long commentId,
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @Valid @RequestBody CommentDto.CommentUpdateCommand command
    ) {
        commentService.updateComment(commentId, user.getUsername(), command);
        return Response.OK;
    }

    @DeleteMapping("/{commentId}")
    @Permit(Role.USER)
    @Operation(description = "댓글 삭제")
    public Response<Void> deleteComment(
        @Parameter(description = "삭제 할 comment id") @PathVariable long commentId,
        @Parameter(hidden = true) @AuthenticationPrincipal User user
    ) {
        commentService.deleteComment(commentId, user.getUsername());
        return Response.OK;
    }

    @GetMapping("/{commentId}")
    @Operation(description = "댓글 상세 조회")
    public Response<CommentDto.GetComment> getComment(
        @Parameter(description = "조회 할 comment id") @PathVariable long commentId
    ) {
        return Response.of(commentService.getComment(commentId));
    }

    @PostMapping("/{commentId}")
    @Permit(Role.USER)
    @Operation(description = "하위 댓글 생성")
    public Response<Void> saveChildComment(
        @Parameter(description = "하위 댓글 생성 할 comment id") @PathVariable long commentId,
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @Valid @RequestBody CommentDto.ChildCommentCreateCommand command
    ) {
        commentService.saveChildComment(commentId, user.getUsername(), command);
        return Response.OK;
    }

    @GetMapping("")
    @Operation(description = "댓글 리스트 조회")
    public Response<DefaultPage<GetComment>> getComments(
        @Parameter(description = "댓글 조회 할 post id") @RequestParam long postId,
        @Parameter(description = "page no.") @RequestParam int page,
        @Parameter(description = "page size") @RequestParam int size
    ) {
        return Response.of(commentService.getComments(CommentType.ROOT, postId, page, size));
    }

    @GetMapping("/{commentId}" + COMMENT_API)
    @Operation(description = "하위 댓글 리스트 조회")
    public Response<DefaultPage<GetComment>> getChildComments(
        @Parameter(description = "하위 댓글 조회 할 comment id") @PathVariable long commentId,
        @Parameter(description = "page no.") @RequestParam int page,
        @Parameter(description = "page size") @RequestParam int size
    ) {
        return Response.of(commentService.getComments(CommentType.CHILD, commentId, page, size));
    }
}
