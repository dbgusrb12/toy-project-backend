package com.hg.blog.feign.naver;

import com.hg.blog.feign.naver.dto.NaverBlog;
import com.hg.blog.feign.naver.dto.NaverSort;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@RequiredArgsConstructor
@Validated
public class NaverFeignClientAdapter {

    private final NaverFeignClient naverFeignClient;

    public NaverBlog getBlogList(
        @NotBlank(message = "필수 값입니다") String query,
        @Min(1) @Max(1000) Integer start,
        @Min(1) @Max(100) Integer display,
        @NotNull(message = "필수 값입니다") NaverSort sort
    ) {
        return naverFeignClient.getBlogList(query, start, display, sort.getSort());
    }
}
