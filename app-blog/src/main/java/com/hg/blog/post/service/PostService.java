package com.hg.blog.post.service;

import com.hg.blog.post.dto.PostDto;

public interface PostService {

    long savePost(PostDto.PostCreateCommand command);
}
