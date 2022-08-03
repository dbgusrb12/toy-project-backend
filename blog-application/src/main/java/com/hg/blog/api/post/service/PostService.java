package com.hg.blog.api.post.service;

import com.hg.blog.api.post.dto.PostDto.PostCreateCommand;

public interface PostService {

    long savePost(PostCreateCommand command);
}
