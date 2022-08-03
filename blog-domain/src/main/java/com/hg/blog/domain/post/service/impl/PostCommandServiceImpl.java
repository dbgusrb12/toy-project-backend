package com.hg.blog.domain.post.service.impl;

import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.service.PostCommandService;
import com.hg.blog.domain.post.entity.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCommandServiceImpl implements PostCommandService {

    private final PostRepository postRepository;

    @Override
    @Transactional
    public Post savePost(Post post) {
        return postRepository.save(post);
    }
}
