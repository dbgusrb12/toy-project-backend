package com.hg.blog.feign.kakao;

import com.hg.blog.feign.kakao.dto.KakaoBlog;
import com.hg.blog.feign.kakao.dto.KakaoSort;
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
public class KakaoFeignClientAdapter {

    private final KakaoFeignClient kakaoFeignClient;

    public KakaoBlog getBlogList(
        @NotBlank(message = "필수 값입니다") String query,
        @Min(1) @Max(50) Integer page,  // default 1
        @Min(1) @Max(50) Integer size,  // default 10
        @NotNull(message = "필수 값입니다") KakaoSort sort
    ) {
        return kakaoFeignClient.getBlogList(query, page, size, sort.getSort());
    }
}
