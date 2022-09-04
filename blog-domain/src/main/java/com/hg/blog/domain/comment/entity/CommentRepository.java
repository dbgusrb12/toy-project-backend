package com.hg.blog.domain.comment.entity;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndDeleted(long id, boolean deleted);

    Page<Comment> findByPost_IdAndParentCommentIsNullAndDeleted(long postId, boolean deleted, Pageable pageable);

    Page<Comment> findByParentComment_IdAndDeleted(long commentId, boolean deleted, Pageable pageable);
}
