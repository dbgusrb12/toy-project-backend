package com.hg.blog.response;

import lombok.Data;

@Data
public class Response<T> {

    private ResponseCode code;
    private String message;
    private T body;

    public static final Response<Void> OK = new Response<>();

    private Response() {
        this.code = ResponseCode.OK;
        this.message = code.getMessage();
    }

    private Response(T body) {
        this();
        this.body = body;
    }

    public static <T> Response<T> of(T body) {
        return new Response<>(body);
    }
}
