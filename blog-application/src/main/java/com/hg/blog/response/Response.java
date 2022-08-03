package com.hg.blog.response;

import lombok.Data;

@Data
public class Response<T> {

    private ResponseCode code;
    private String message;
    private T body;

    private Response() {
        this.code = ResponseCode.OK;
        this.message = code.getMessage();
    }

    private Response(T body) {
        this();
        this.body = body;
    }

    public static Response ok() {
        return new Response();
    }

    public static <T> Response of(T body) {
        return new Response(body);
    }
}
