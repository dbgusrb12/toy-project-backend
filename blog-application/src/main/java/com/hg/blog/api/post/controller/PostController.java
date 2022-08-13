package com.hg.blog.api.post.controller;

import static com.hg.blog.constants.Constants.API_PREFIX;
import static com.hg.blog.constants.Constants.POST_API;

import com.hg.blog.annotation.Permit;
import com.hg.blog.annotation.Role;
import com.hg.blog.api.post.dto.PostDto;
import com.hg.blog.api.post.service.PostService;
import com.hg.blog.response.Response;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + POST_API)
public class PostController {

    private final PostService postService;

    @PostMapping("")
    @Permit(role = Role.USER)
    public Response<Long> savePost(@RequestAttribute String userId,
        @Valid @RequestBody PostDto.PostCreateCommand command) {
        return Response.of(postService.savePost(userId, command));
    }

}
