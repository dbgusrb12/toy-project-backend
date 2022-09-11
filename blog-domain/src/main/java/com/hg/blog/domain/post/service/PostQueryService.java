package com.hg.blog.domain.post.service;

import com.hg.blog.domain.dto.DefaultPage;
import com.hg.blog.domain.keyword.event.KeywordEventPublisher;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.entity.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostQueryService {

    private final PostRepository postRepository;
    private final KeywordEventPublisher keywordEventPublisher;

    public Post getPost(long postId) {
        return postRepository.findByIdAndDeleted(postId, false)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    public DefaultPage<Post> getPosts(String search, int page, int size) {
        search = getSearch(search);
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "created"));
        Page<Post> posts = postRepository.findByContentContainsAndDeleted(search, false, pageable);
        return DefaultPage.of(posts);
    }

    private String getSearch(String search) {
        if (search == null || search.isBlank()) {
            return "";
        }
        keywordEventPublisher.keywordEvent(search);
        return search;
    }
}
