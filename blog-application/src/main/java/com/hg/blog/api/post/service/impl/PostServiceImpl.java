package com.hg.blog.api.post.service.impl;

import com.hg.blog.api.post.dto.PostDto.PostCreateCommand;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.service.PostCommandService;
import com.hg.blog.api.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostCommandService postCommandService;

    @Override
    public long savePost(PostCreateCommand command) {
        Post post = postCommandService.savePost(command.toEntity());
        return post.getId();
    }
}
