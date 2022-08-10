package com.hg.blog.api.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class PostDto {

    @Getter
    @AllArgsConstructor
    public static class PostCreateCommand {

        private String title;
        private String content;

    }
}
