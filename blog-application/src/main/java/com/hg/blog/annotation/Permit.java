package com.hg.blog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Permit {

    @AliasFor("role")
    Role value() default Role.USER;

    @AliasFor("value")
    Role role() default Role.USER;
}
