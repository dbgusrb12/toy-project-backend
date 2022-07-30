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

    public Response(T body) {
        this();
        this.body = body;
    }

    public static Response ok() {
        return new Response();
    }
}
