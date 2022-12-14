package com.hg.blog.api.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class PostDto {

    @Getter
    @AllArgsConstructor
    public static class PostCreateCommand {

        @NotBlank(message = "필수 값입니다")
        @Schema(description = "게시글 제목")
        private String title;
        @Schema(description = "게시글 내용")
        private String content;

    }

    @Getter
    @AllArgsConstructor
    public static class PostUpdateCommand {

        @NotBlank(message = "필수 값입니다")
        @Schema(description = "게시글 제목")
        private String title;
        @NotBlank(message = "필수 값입니다")
        @Schema(description = "게시글 내용")
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

    @Getter
    @AllArgsConstructor
    public static class GetPostList {

        // BlogType 이 IN_APP 일 경우 link null
        // BlogType 이 NAVER, KAKAO 일 경우 id null, name 은 blog 이름
        private BlogType blogType;
        private Long id;
        private String title;
        private String content;
        private String name;
        private String link;
        private LocalDateTime created;
    }
}
