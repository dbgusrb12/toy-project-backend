package com.hg.blog.api.keyword.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.hg.blog.api.keyword.dto.KeywordDto.GetKeywordList;
import com.hg.blog.domain.keyword.projection.KeywordGrouping;
import com.hg.blog.domain.keyword.service.KeywordQueryService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KeywordServiceTest {

    @Mock
    KeywordQueryService keywordQueryService;

    @InjectMocks
    KeywordService keywordService;

    @Test
    void getKeywordListTest() {
        // given
        List<KeywordGrouping> keywords = getKeywordList();
        given(keywordQueryService.getKeywordTop10())
            .willReturn(keywords);

        // when
        List<GetKeywordList> keywordList = keywordService.getKeywordList();

        // then
        assertThat(keywordList).isNotNull();
        assertThat(keywordList.size()).isEqualTo(10);
    }

    private List<KeywordGrouping> getKeywordList() {
        List<KeywordGrouping> keywordGroupings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            keywordGroupings.add(new KeywordGrouping(i, "test" + i));
        }
        return keywordGroupings;
    }
}