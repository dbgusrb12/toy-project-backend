package com.hg.blog.api.post.controller;

import static com.hg.blog.constants.Constants.API_PREFIX;
import static com.hg.blog.constants.Constants.POST_API;

import com.hg.blog.annotation.Permit;
import com.hg.blog.annotation.Role;
import com.hg.blog.api.post.dto.BlogType;
import com.hg.blog.api.post.dto.PostDto;
import com.hg.blog.api.post.dto.PostDto.GetPostList;
import com.hg.blog.api.post.service.PostSearchService;
import com.hg.blog.api.post.service.PostService;
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
@RequestMapping(API_PREFIX + POST_API)
@Tag(name = "post")
public class PostController {

    private final PostService postService;
    private final PostSearchService postSearchService;

    @PostMapping("")
    @Permit(Role.USER)
    @Operation(description = "게시글 생성")
    public Response<Void> savePost(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @Valid @RequestBody PostDto.PostCreateCommand command
    ) {
        postService.savePost(user.getUsername(), command);
        return Response.ok();
    }

    @PutMapping("/{postId}")
    @Permit(Role.USER)
    @Operation(description = "게시글 수정")
    public Response<Void> updatePost(
        @Parameter(description = "수정 할 post id") @PathVariable long postId,
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @Valid @RequestBody PostDto.PostUpdateCommand command
    ) {
        postService.updatePost(postId, user.getUsername(), command);
        return Response.ok();
    }

    @DeleteMapping("/{postId}")
    @Permit(Role.USER)
    @Operation(description = "게시글 삭제")
    public Response<Void> deletePost(
        @Parameter(description = "삭제 할 post id") @PathVariable long postId,
        @Parameter(hidden = true) @AuthenticationPrincipal User user
    ) {
        postService.deletePost(postId, user.getUsername());
        return Response.ok();
    }

    @GetMapping("/{postId}")
    @Operation(description = "게시글 상세 조회")
    public Response<PostDto.GetPost> getPost(@Parameter(description = "조회 할 post id") @PathVariable long postId) {
        return Response.of(postService.getPost(postId));
    }

    @GetMapping("")
    @Operation(description = "게시글 리스트 조회")
    public Response<DefaultPage<GetPostList>> getPosts(
        @Parameter(description = "검색 할 blog 타입") @RequestParam BlogType blogType,
        @Parameter(description = "검색 할 내용") @RequestParam(required = false) String search,
        @Parameter(description = "page no") @RequestParam int page,
        @Parameter(description = "page size") @RequestParam int size
    ) {
        return Response.of(postSearchService.getPosts(blogType, search, page, size));
    }

}
