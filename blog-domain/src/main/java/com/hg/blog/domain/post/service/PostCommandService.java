package com.hg.blog.domain.post.service;

import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.service.AccountQueryService;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.entity.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCommandService {

    private final AccountQueryService accountQueryService;
    private final PostRepository postRepository;

    @Transactional
    public Post savePost(String userId, String title, String content) {
        Account owner = accountQueryService.getAccountByUserId(userId);
        Post post = Post.of(owner, title, content);
        return postRepository.save(post);
    }
}
