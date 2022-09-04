package com.hg.blog.api.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommentType {
    ROOT,
    CHILD;

    public boolean isRoot() {
        return this == ROOT;
    }
}
