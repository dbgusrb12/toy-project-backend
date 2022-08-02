package com.hg.blog.entity.post.impl;

import com.hg.blog.entity.post.PostQueryService;
import com.hg.blog.entity.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;
}
