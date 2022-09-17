package com.hg.blog.api.keyword.controller;

import static com.hg.blog.constants.Constants.API_PREFIX;
import static com.hg.blog.constants.Constants.KEYWORD_API;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.hg.blog.api.keyword.dto.KeywordDto.GetKeywordList;
import com.hg.blog.api.keyword.service.KeywordService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(KeywordController.class)
class KeywordControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    KeywordService keywordService;

    @Test
    void getKeywordsTest() throws Exception {
        // given
        given(keywordService.getKeywords())
            .willReturn(getKeywordList());

        // when, then
        mockMvc.perform(get(API_PREFIX + KEYWORD_API)
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body", hasSize(10)))
            .andDo(print());
    }

    private List<GetKeywordList> getKeywordList() {
        List<GetKeywordList> keywordGroupings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            keywordGroupings.add(new GetKeywordList(i, "test" + i));
        }
        return keywordGroupings;
    }

}