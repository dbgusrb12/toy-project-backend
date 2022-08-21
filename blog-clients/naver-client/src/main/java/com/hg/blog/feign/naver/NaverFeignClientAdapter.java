package com.hg.blog.feign.naver;

import com.hg.blog.feign.naver.dto.BlogResult;
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

    public BlogResult getBlogList(
        @NotBlank String query,
        @Min(1) @Max(100) Long display,
        @Min(1) @Max(100) Long start,
        @NotNull NaverSort sort
    ) {
        return naverFeignClient.getBlogList(query, display, start, sort.getSort());
    }
}
