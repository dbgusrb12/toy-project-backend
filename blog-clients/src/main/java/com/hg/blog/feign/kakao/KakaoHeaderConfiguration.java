package com.hg.blog.feign.kakao;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class KakaoHeaderConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor(
        @Value("${kakao.rest-api-key}") String restApiKey
    ) {
        return requestTemplate ->requestTemplate
            .header("Authorization", "KakaoAK " + restApiKey);
    }
}
