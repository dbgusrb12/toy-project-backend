package com.hg.blog.feign.naver;

import com.hg.blog.feign.naver.dto.BlogResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "NaverClient", url = "https://openapi.naver.com/v1", configuration = NaverHeaderConfiguration.class)
interface NaverFeignClient {

    @GetMapping("/search/blog.json")
    BlogResult getBlogList(
        @RequestParam("query") String query,
        @RequestParam(value = "display", required = false) Long display,
        @RequestParam(value = "start", required = false) Long start,
        @RequestParam(value = "sort", required = false) String sort
    );
}
