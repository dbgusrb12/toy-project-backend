package com.hg.blog.entity.post.impl;

import com.hg.blog.entity.post.Post;
import com.hg.blog.entity.post.PostCommandService;
import com.hg.blog.entity.post.PostRepository;
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
