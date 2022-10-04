package com.hg.blog.feign.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum KakaoSort {
    ACCURACY("accuracy"),
    RECENCY("recency");

    private final String sort;
}
