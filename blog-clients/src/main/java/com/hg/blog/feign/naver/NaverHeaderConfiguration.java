package com.hg.blog.feign.naver;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class NaverHeaderConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor(
        @Value("${naver.client-id}") String clientId,
        @Value("${naver.client-secret}") String clientSecret
    ) {
        return requestTemplate -> requestTemplate
            .header("X-Naver-Client-Id", clientId)
            .header("X-Naver-Client-Secret", clientSecret);
    }
}
