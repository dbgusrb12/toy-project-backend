package com.hg.blog.api.post.service;

import com.hg.blog.api.post.dto.PostDto.GetPost;
import com.hg.blog.api.post.dto.PostDto.PostCreateCommand;
import com.hg.blog.api.post.dto.PostDto.PostUpdateCommand;
import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.service.AccountQueryService;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.service.PostCommandService;
import com.hg.blog.domain.post.service.PostQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;
    private final AccountQueryService accountQueryService;

    public long savePost(String userId, PostCreateCommand request) {
        Account owner = accountQueryService.getAccountByUserId(userId);
        Post post = postCommandService.savePost(owner, request.getTitle(), request.getContent());
        return post.getId();
    }

    public long updatePost(long postId, String userId, PostUpdateCommand command) {
        Account owner = accountQueryService.getAccountByUserId(userId);
        Post post = postCommandService.updatePost(
            owner,
            postId,
            command.getTitle(),
            command.getContent()
        );
        return post.getId();
    }

    public void deletePost(long postId, String userId) {
        Account owner = accountQueryService.getAccountByUserId(userId);
        postCommandService.deletePost(owner, postId);
    }

    public GetPost getPost(long postId) {
        Post post = postQueryService.getPost(postId);
        return GetPost.of(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getAccount().getNickname(),
            post.getCreated(),
            post.getUpdated()
        );
    }
}
