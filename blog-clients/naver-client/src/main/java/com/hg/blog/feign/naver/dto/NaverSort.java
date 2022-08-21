package com.hg.blog.feign.naver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NaverSort {
    SIM("sim"),
    DATE("date");

    private String sort;
}
