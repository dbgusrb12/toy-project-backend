package com.hg.blog.api.post.controller;

import static com.hg.blog.constants.Constants.API_PREFIX;
import static com.hg.blog.constants.Constants.POST_API;

import com.hg.blog.annotation.Permit;
import com.hg.blog.annotation.Role;
import com.hg.blog.api.post.dto.PostDto;
import com.hg.blog.api.post.dto.PostDto.GetPost;
import com.hg.blog.api.post.service.PostService;
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
@RequestMapping(API_PREFIX + POST_API)
public class PostController {

    private final PostService postService;

    @PostMapping("")
    @Permit(Role.USER)
    public Response<Void> savePost(
        @RequestAttribute String userId,
        @Valid @RequestBody PostDto.PostCreateCommand command
    ) {
        postService.savePost(userId, command);
        return Response.ok();
    }

    @PutMapping("/{postId}")
    @Permit(Role.USER)
    public Response<Void> updatePost(
        @PathVariable long postId,
        @RequestAttribute String userId,
        @Valid @RequestBody PostDto.PostUpdateCommand command
    ) {
        postService.updatePost(postId, userId, command);
        return Response.ok();
    }

    @DeleteMapping("/{postId}")
    @Permit(Role.USER)
    public Response<Void> deletePost(@PathVariable long postId, @RequestAttribute String userId) {
        postService.deletePost(postId, userId);
        return Response.ok();
    }

    @GetMapping("/{postId}")
    public Response<PostDto.GetPost> getPost(@PathVariable long postId) {
        return Response.of(postService.getPost(postId));
    }

    @GetMapping("")
    public Response<DefaultPage<GetPost>> getPosts(
        @RequestParam(required = false) String search,
        @RequestParam int page,
        @RequestParam int size
    ) {
        return Response.of(postService.getPosts(search, page, size));
    }

}
