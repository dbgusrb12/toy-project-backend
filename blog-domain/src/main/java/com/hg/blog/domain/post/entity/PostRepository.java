package com.hg.blog.domain.post.entity;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndDeleted(long id, boolean deleted);

    Page<Post> findByContentContainsAndDeleted(String content, boolean deleted, Pageable pageable);

    Page<Post> findByDeleted(boolean deleted, Pageable pageable);
}
