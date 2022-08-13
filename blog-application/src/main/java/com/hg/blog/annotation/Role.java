package com.hg.blog.annotation;

public enum Role {
    USER,
    ALL;

    public boolean permitUser() {
        return this == USER;
    }

    public boolean permitAll() {
        return this == ALL;
    }
}
