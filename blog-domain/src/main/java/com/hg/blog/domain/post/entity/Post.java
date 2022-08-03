package com.hg.blog.domain.post.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 Protected 로 지정
public class Post {

    @Builder(access = AccessLevel.PRIVATE)
    private Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Id
    @GeneratedValue
    private long id;

    private String title;

    private String content;

    @Column(updatable = false)
    private LocalDateTime created = LocalDateTime.now();

    @Column(updatable = false)
    private LocalDateTime updated = LocalDateTime.now();

    private boolean deletedYn;

    public static Post of(String title, String content) {
        return Post.builder()
            .title(title)
            .content(content)
            .build();
    }

}
