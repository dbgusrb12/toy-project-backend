package com.hg.blog.domain.post.service.impl;

import com.hg.blog.domain.post.service.PostQueryService;
import com.hg.blog.domain.post.entity.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;
}
