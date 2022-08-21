package com.hg.blog.domain.comment.entity;

import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.post.entity.Post;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    private Comment(Account account, Post post, String content) {
        this.account = account;
        this.post = post;
        this.content = content;
    }

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Account account;

    @ManyToOne
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    private List<Comment> childComment = new ArrayList<>();

    private String content;

    @Column(updatable = false)
    private LocalDateTime created = LocalDateTime.now();

    @Column(updatable = false)
    private LocalDateTime updated = LocalDateTime.now();

    private boolean deleted;

    public static Comment of(Account account, Post post, String content) {
        return new Comment(account, post, content);
    }

    public void addComment(Comment comment) {
        this.childComment.add(comment);
        comment.parentComment = this;
    }
}
