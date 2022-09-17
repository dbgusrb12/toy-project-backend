package com.hg.blog.domain.keyword.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.blog.domain.config.QueryDslConfig;
import com.hg.blog.domain.keyword.entity.Keyword;
import com.hg.blog.domain.keyword.entity.KeywordCustomRepository;
import com.hg.blog.domain.keyword.entity.KeywordRepository;
import com.hg.blog.domain.keyword.projection.KeywordGrouping;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import({KeywordQueryService.class, QueryDslConfig.class, KeywordCustomRepository.class})
@ActiveProfiles({"blog-domain", "local"})
class KeywordQueryServiceTest {

    @Autowired
    KeywordQueryService keywordQueryService;

    @Autowired
    KeywordRepository keywordRepository;


    @Test
    void getKeywordTop10Test() {
        // given
        saveKeywordList();

        // when
        List<KeywordGrouping> keywordGroupings = keywordQueryService.getKeywordTop10();

        // then
        assertThat(keywordGroupings).isNotNull();
        assertThat(keywordGroupings.size()).isEqualTo(2);
    }

    private void saveKeywordList() {
        List<Keyword> keywords = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            keywords.add(Keyword.of("content" + i % 2));
        }
        keywordRepository.saveAll(keywords);
    }
}