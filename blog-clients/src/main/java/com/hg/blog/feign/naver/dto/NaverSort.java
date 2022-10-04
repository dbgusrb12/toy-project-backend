package com.hg.blog.feign.naver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NaverSort {
    SIM("sim"),     // 정확도순
    DATE("date");   // 최신순

    private final String sort;
}
