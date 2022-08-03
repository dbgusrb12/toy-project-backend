package com.hg.blog.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ResponseCode {
    OK("정상적으로 처리되었습니다.");
    private final String message;

    public String getMessage() {
        return message;
    }
}
