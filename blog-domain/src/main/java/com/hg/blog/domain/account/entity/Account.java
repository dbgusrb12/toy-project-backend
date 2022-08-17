package com.hg.blog.domain.account.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    private Account(String userId, String password, String nickname) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
    }

    @Id
    @GeneratedValue
    private long id;

    private String userId;

    private String password;

    private String nickname;

    @Column(updatable = false)
    private LocalDateTime created = LocalDateTime.now();

    @Column(updatable = false)
    private LocalDateTime updated = LocalDateTime.now();

    private boolean deletedYn;

    public static Account of(String userId, String password, String nickname) {
        return new Account(userId, password, nickname);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Account)) {
            return false;
        }

        Account account = (Account) o;
        return this.userId.equals(account.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }
}
