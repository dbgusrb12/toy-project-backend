package com.hg.blog.api.post.service;

import com.hg.blog.api.post.dto.PostDto.PostCreateCommand;
import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.service.AccountQueryService;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.service.PostCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostCommandService postCommandService;
    private final AccountQueryService accountQueryService;

    public long savePost(String userId, PostCreateCommand request) {
        Account owner = accountQueryService.getAccountByUserId(userId);
        Post post = postCommandService.savePost(owner, request.getTitle(), request.getContent());
        return post.getId();
    }
}
