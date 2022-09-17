package com.hg.blog.domain.keyword.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.blog.domain.config.QueryDslConfig;
import com.hg.blog.domain.keyword.projection.KeywordGrouping;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles({"blog-domain", "local"})
@Import({QueryDslConfig.class, KeywordCustomRepository.class})
class KeywordGroupingCustomRepositoryTest {

    @Autowired
    private KeywordCustomRepository keywordCustomRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    @Test
    public void getKeywordGroupingListTop10Test() {
        // given
        saveKeywordList();

        // when
        List<KeywordGrouping> keywordList = keywordCustomRepository.getKeywordGroupingListTop10();

        // then
        assertThat(keywordList).isNotNull();
        assertThat(keywordList.size()).isEqualTo(2);
    }

    private void saveKeywordList() {
        List<Keyword> keywords = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            keywords.add(Keyword.of("content" + i % 2));
        }
        keywordRepository.saveAll(keywords);
    }
}