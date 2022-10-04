package com.hg.blog.feign.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum KakaoSort {
    ACCURACY("accuracy"),   // 정확도순
    RECENCY("recency");     // 최신순

    private final String sort;
}
