package com.hg.blog.api.comment.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentCreateCommand {

        @NotNull(message = "필수 값입니다.")
        private Long postId;

        @NotBlank(message = "필수 값입니다.")
        private String content;
    }
}
