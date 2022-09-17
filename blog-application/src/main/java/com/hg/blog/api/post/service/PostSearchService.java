package com.hg.blog.api.post.service;

import com.hg.blog.api.post.dto.BlogType;
import com.hg.blog.api.post.dto.PostDto.GetPostList;
import com.hg.blog.domain.dto.DefaultPage;

public interface PostSearchService {

    default DefaultPage<GetPostList> getPosts(BlogType blogType, String search, int page, int size) {
        throw new UnsupportedOperationException();
    };

    DefaultPage<GetPostList> getPosts(String search, int page, int size);
}
