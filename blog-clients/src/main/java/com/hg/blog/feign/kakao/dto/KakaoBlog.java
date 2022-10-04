package com.hg.blog.feign.kakao.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoBlog {

    private Meta meta;
    private List<Document> documents;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {

        private long total_count;
        private long pageable_count;
        private boolean is_end;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Document {

        private String title;
        private String contents;
        private String url;
        private String blogname;
        private String thumbnail;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        private LocalDateTime datetime;
    }
}
