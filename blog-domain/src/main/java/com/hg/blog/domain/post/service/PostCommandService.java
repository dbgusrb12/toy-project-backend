package com.hg.blog.domain.post.service;

import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.entity.PostRepository;
import java.security.AccessControlException;
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
    public Post updatePost(Account owner, long postId, String title, String content) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        // TODO 삭제된 게시글에 대한 정책 수립 필요. 삭제된 게시글로 표시할건지, 아니면 존재하지 않는 게시글로 표시할건지.
        checkDeletedPost(post);
        checkUpdatePermission(owner, post);
        post.updatePost(title, content);
        return postRepository.save(post);
    }

    private void checkDeletedPost(Post post) {
        if (post.isDeleted()) {
            throw new IllegalArgumentException("삭제된 게시글입니다.");
        }
    }

    private void checkUpdatePermission(Account account, Post post) {
        if (post.getAccount() != account) {
            throw new AccessControlException("권한이 없습니다.");
        }
    }
}
