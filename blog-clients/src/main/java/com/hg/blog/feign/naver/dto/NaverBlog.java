package com.hg.blog.feign.naver.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NaverBlog {

    @JsonFormat(pattern = "EEE, dd MMM yyyy HH:mm:ss Z", locale = "en")
    private LocalDateTime lastBuildDate;
    private long total;
    private long start;
    private long display;
    private List<Item> items;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {

        private String title;
        private String link;
        private String description;
        private String bloggername;
        private String bloggerlink;
        @JsonFormat(pattern = "yyyyMMdd")
        private LocalDate postdate;
    }
}
