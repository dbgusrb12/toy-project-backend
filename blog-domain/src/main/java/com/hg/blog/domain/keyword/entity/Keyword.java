package com.hg.blog.domain.keyword.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {

    private Keyword(String content) {
        this.content = content;
    }

    @Id
    @GeneratedValue
    private long id;

    private String content;

    private LocalDateTime created = LocalDateTime.now();

    private LocalDateTime updated = LocalDateTime.now();

    private boolean deleted;

    public static Keyword of(String content) {
        return new Keyword(content);
    }
}
