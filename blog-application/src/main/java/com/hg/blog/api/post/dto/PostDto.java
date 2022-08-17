package com.hg.blog.api.post.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class PostDto {

    @Getter
    @AllArgsConstructor
    public static class PostCreateCommand {

        @NotBlank(message = "필수 값입니다.")
        private String title;
        private String content;

    }

    @Getter
    @AllArgsConstructor
    public static class PostUpdateCommand {

        @NotBlank(message = "필수 값입니다.")
        private String title;
        @NotBlank(message = "필수 값입니다.")
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public static class GetPost {

        private long id;
        private String title;
        private String content;
        private String nickname;
        private LocalDateTime created;
        private LocalDateTime updated;

        public static GetPost of(
            long id,
            String title,
            String content,
            String nickname,
            LocalDateTime created,
            LocalDateTime updated
        ) {
            return new GetPost(id, title, content, nickname, created, updated);
        }
    }
}
