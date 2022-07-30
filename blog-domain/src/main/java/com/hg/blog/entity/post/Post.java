package com.hg.blog.entity.post;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 Protected 로 지정
@EntityListeners(value = AuditingEntityListener.class) // Entity Auditing Listener 등록
@DynamicInsert // null 인 값은 제외하고 insert 쿼리가 날아감
@DynamicUpdate // null 인 값은 제외하고 update 쿼리가 날아감
public class Post {

    @Id
    @GeneratedValue
    private long id;

    private String title;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created;

    @LastModifiedDate
    @Column(updatable = false)
    private LocalDateTime updated;

    private boolean deletedYn;


}
