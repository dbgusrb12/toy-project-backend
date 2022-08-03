package com.hg.blog.api.post.service;

import com.hg.blog.api.post.dto.PostDto.PostCreateCommand;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.service.PostCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostCommandService postCommandService;

    public long savePost(PostCreateCommand command) {
        Post post = postCommandService.savePost(command.toEntity());
        return post.getId();
    }
}
