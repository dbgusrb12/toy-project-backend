package com.hg.blog.api.post.service;

import com.hg.blog.api.post.dto.BlogType;
import com.hg.blog.api.post.dto.PostDto.GetPostList;
import com.hg.blog.domain.dto.DefaultPage;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.service.PostQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InAppPostSearchService implements PostSearchService {

    private final PostQueryService postQueryService;

    @Override
    @Transactional
    public DefaultPage<GetPostList> getPosts(String search, int page, int size) {
        DefaultPage<Post> posts = postQueryService.getPosts(search, page, size);
        return posts.map(post ->
            new GetPostList(
                BlogType.IN_APP,
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAccount().getNickname(),
                "",
                post.getCreated()
            )
        );
    }
}
