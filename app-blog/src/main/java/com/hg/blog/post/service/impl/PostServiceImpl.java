package com.hg.blog.post.service.impl;

import com.hg.blog.entity.post.Post;
import com.hg.blog.entity.post.PostCommandService;
import com.hg.blog.post.dto.PostDto.PostCreateCommand;
import com.hg.blog.post.service.PostService;
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
