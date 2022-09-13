package com.hg.blog.domain.keyword.entity;

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
    public void sample() {
        List<Keyword> keywords = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            keywords.add(Keyword.of("content" + i % 2));
        }
        keywordRepository.saveAll(keywords);
        List<KeywordGrouping> sample = keywordCustomRepository.sample();

        sample.forEach(item -> {
            System.out.println(item.getContent());
            System.out.println(item.getCount());
        });
    }
}