package com.hg.blog.domain.post.entity;

import com.hg.blog.domain.account.entity.Account;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 Protected 로 지정
public class Post {

    private Post(Account account, String title, String content) {
        this.account = account;
        this.title = title;
        this.content = content;
    }

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Account account;

    private String title;

    private String content;

    @Column(updatable = false)
    private LocalDateTime created = LocalDateTime.now();

    @Column(updatable = false)
    private LocalDateTime updated = LocalDateTime.now();

    private boolean deletedYn;

    public static Post of(Account account, String title, String content) {
        return new Post(account, title, content);
    }

}
