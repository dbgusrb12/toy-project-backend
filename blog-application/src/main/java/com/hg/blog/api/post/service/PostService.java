package com.hg.blog.api.post.service;

import com.hg.blog.api.post.dto.PostDto.GetPost;
import com.hg.blog.api.post.dto.PostDto.PostCreateCommand;
import com.hg.blog.api.post.dto.PostDto.PostUpdateCommand;
import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.service.AccountQueryService;
import com.hg.blog.domain.dto.DefaultPage;
import com.hg.blog.domain.keyword.service.KeywordCommandService;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.service.PostCommandService;
import com.hg.blog.domain.post.service.PostQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;
    private final AccountQueryService accountQueryService;
    private final KeywordCommandService keywordCommandService;

    @Transactional
    public void savePost(String userId, PostCreateCommand request) {
        final Account owner = accountQueryService.getAccountByUserId(userId);
        postCommandService.savePost(owner, request.getTitle(), request.getContent());
    }

    @Transactional
    public void updatePost(long postId, String userId, PostUpdateCommand command) {
        final Account owner = accountQueryService.getAccountByUserId(userId);
        postCommandService.updatePost(owner, postId, command.getTitle(), command.getContent());
    }

    @Transactional
    public void deletePost(long postId, String userId) {
        final Account owner = accountQueryService.getAccountByUserId(userId);
        postCommandService.deletePost(owner, postId);
    }

    public GetPost getPost(long postId) {
        final Post post = postQueryService.getPost(postId);
        return GetPost.of(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getAccount().getNickname(),
            post.getCreated(),
            post.getUpdated()
        );
    }

    public DefaultPage<GetPost> getPosts(String search, int page, int size) {
        addKeyword(search);
        DefaultPage<Post> posts = postQueryService.getPosts(search, page, size);
        return posts.map(post ->
            GetPost.of(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAccount().getNickname(),
                post.getCreated(),
                post.getUpdated()
            )
        );
    }

    private void addKeyword(String search) {
        if (StringUtils.hasText(search)) {
            keywordCommandService.addKeywordEvent(search);
        }
    }
}
