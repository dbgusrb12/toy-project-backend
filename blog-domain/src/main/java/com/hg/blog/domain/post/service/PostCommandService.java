package com.hg.blog.domain.post.service;

import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.entity.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCommandService {

    private final PostRepository postRepository;

    @Transactional
    public Post savePost(String title, String content) {
        Post post = Post.of(title, content);
        return postRepository.save(post);
    }
}
