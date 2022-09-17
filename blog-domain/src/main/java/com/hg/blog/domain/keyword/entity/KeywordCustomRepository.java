package com.hg.blog.domain.keyword.entity;


import static com.hg.blog.domain.keyword.entity.QKeyword.keyword;

import com.hg.blog.domain.keyword.projection.KeywordGrouping;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KeywordCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<KeywordGrouping> getKeywordGroupingListTop10() {
        JPAQuery<KeywordGrouping> query = jpaQueryFactory
            .from(keyword)
            .select(
                Projections.bean(
                    KeywordGrouping.class,
                    keyword.content.as("content"),
                    keyword.content.count().as("count")
                )
            )
            .groupBy(keyword.content)
            .orderBy(new OrderSpecifier<>(Order.DESC, keyword.content.count()))
            .limit(10);

        return query.fetch();
    }
}
