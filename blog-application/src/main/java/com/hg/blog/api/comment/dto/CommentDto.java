package com.hg.blog.api.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "댓글 생성 할 post id")
        private Long postId;

        @NotBlank(message = "필수 값입니다.")
        @Schema(description = "댓글 내용")
        private String content;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChildCommentCreateCommand {

        @NotBlank(message = "필수 값입니다.")
        @Schema(description = "댓글 내용")
        private String content;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentUpdateCommand {

        @NotBlank(message = "필수 값입니다.")
        @Schema(description = "수정 할 댓글 내용")
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public static class GetComment {

        private long id;
        private long postId;
        private Long parentCommentId;
        private String content;
        private String nickname;
        private LocalDateTime created;
        private LocalDateTime updated;

        public static GetComment of(
            long id,
            long postId,
            Long parentCommentId,
            String content,
            String nickname,
            LocalDateTime created,
            LocalDateTime updated
        ) {
            return new GetComment(id, postId, parentCommentId, content, nickname, created, updated);
        }
    }
}
