package com.hg.blog.domain.post.service;

import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.entity.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostQueryService {

    private final PostRepository postRepository;

    public Post getPost(long postId) {
        return postRepository.findByIdAndDeleted(postId, false)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }
}
