package com.hg.blog.domain.post.service;

import com.hg.blog.domain.post.entity.Post;

public interface PostCommandService {

    Post savePost(Post post);
}
