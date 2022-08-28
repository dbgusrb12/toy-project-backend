package com.hg.blog.api.comment.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentDto {

    @Getter
    @AllArgsConstructor
    public static class CommentCreateCommand {

        @NotNull(message = "필수 값입니다.")
        private Long postId;

        @NotBlank(message = "필수 값입니다.")
        private String content;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChildCommentCreateCommand {

        @NotBlank(message = "필수 값입니다.")
        private String content;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentUpdateCommand {

        @NotBlank(message = "필수 값입니다.")
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public static class GetComment {

        private long id;
        private String content;
        private String nickname;
        private LocalDateTime created;
        private LocalDateTime updated;

        public static GetComment of(
            long id,
            String content,
            String nickname,
            LocalDateTime created,
            LocalDateTime updated
        ) {
            return new GetComment(id, content, nickname, created, updated);
        }
    }
}
