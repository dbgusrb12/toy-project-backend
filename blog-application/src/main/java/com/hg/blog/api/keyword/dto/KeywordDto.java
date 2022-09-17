package com.hg.blog.api.keyword.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class KeywordDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetKeywordList {
        private long count;
        private String content;
    }
}
