package com.hg.blog.domain.post.service;

import com.hg.blog.domain.post.entity.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostQueryService {

    private final PostRepository postRepository;
}
