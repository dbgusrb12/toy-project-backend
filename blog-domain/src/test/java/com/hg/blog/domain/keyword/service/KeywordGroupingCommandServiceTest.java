package com.hg.blog.domain.keyword.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.blog.domain.keyword.entity.Keyword;
import com.hg.blog.domain.keyword.entity.KeywordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(KeywordCommandService.class)
@ActiveProfiles({"blog-domain", "local"})
class KeywordGroupingCommandServiceTest {

    @Autowired
    KeywordCommandService keywordCommandService;

    @Autowired
    KeywordRepository keywordRepository;

    @Test
    void addKeywordTest() {
        // given
        String content = "search keyword";

        // when
        Keyword keyword = keywordCommandService.addKeyword(content);

        // then
        assertThat(keyword).isNotNull();
        assertThat(keyword.getContent()).isEqualTo("search keyword");
    }
}