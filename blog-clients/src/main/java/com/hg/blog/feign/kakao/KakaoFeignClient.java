package com.hg.blog.feign.kakao;

import com.hg.blog.feign.kakao.dto.KakaoBlog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "KakaoClient", url = "https://dapi.kakao.com/v2", configuration = KakaoHeaderConfiguration.class)
public interface KakaoFeignClient {

    @GetMapping("/search/blog")
    KakaoBlog getBlogList(
        @RequestParam("query") String query,
        @RequestParam(value = "page", required = false) Integer page,
        @RequestParam(value = "size", required = false) Integer size,
        @RequestParam(value = "sort", required = false) String sort
    );
}
