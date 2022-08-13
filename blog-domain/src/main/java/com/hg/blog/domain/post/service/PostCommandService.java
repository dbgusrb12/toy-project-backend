package com.hg.blog.domain.post.service;

import com.hg.blog.domain.account.entity.Account;
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
    public Post savePost(Account owner, String title, String content) {
        Post post = Post.of(owner, title, content);
        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(long postId, String title, String content) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        post.updatePost(title, content);
        return postRepository.save(post);
    }
}
