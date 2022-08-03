package com.hg.blog.api.post.dto;

import com.hg.blog.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PostDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostCreateCommand {

        private String title;
        private String content;

        public Post toEntity() {
            return Post.of(this.title, this.content);
        }
    }
}