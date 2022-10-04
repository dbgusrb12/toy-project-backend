package com.hg.blog.feign.kakao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.hg.blog.feign.kakao.dto.KakaoBlog;
import com.hg.blog.feign.kakao.dto.KakaoSort;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {"env=test", "feign.client.config.default.loggerLevel=FULL"})
@ActiveProfiles({"blog-client", "local"})
class KakaoFeignClientAdapterTest {

    @Autowired
    private KakaoFeignClientAdapter kakaoFeignClientAdapter;

    @Test
    void getBlogsTest() {
        KakaoBlog sample = kakaoFeignClientAdapter.getBlogList("sample", null, null, KakaoSort.ACCURACY);
        assertThat(sample.getDocuments().size()).isEqualTo(10);
    }

    @Test
    void getBlogs_query_가_존재하지_않을_때_에러() {
        Executable execute = () -> kakaoFeignClientAdapter.getBlogList(null, null, null, KakaoSort.ACCURACY);
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("getBlogList.query: 필수 값입니다");
    }

    @Test
    void getBlogs_page_가_1_이하_일_때_에러() {
        Executable execute = () -> kakaoFeignClientAdapter.getBlogList("sample", 0, null, KakaoSort.ACCURACY);
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("getBlogList.page: 1 이상이어야 합니다");
    }

    @Test
    void getBlogs_page_가_50_이상_일_때_에러() {
        Executable execute = () -> kakaoFeignClientAdapter.getBlogList("sample", 51, null, KakaoSort.ACCURACY);
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("getBlogList.page: 50 이하여야 합니다");
    }

    @Test
    void getBlogs_size_가_1_이하_일_때_에러() {
        Executable execute = () -> kakaoFeignClientAdapter.getBlogList("sample", null, 0, KakaoSort.ACCURACY);
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("getBlogList.size: 1 이상이어야 합니다");
    }

    @Test
    void getBlogs_size_가_50_이상_일_때_에러() {
        Executable execute = () -> kakaoFeignClientAdapter.getBlogList("sample", null, 51, KakaoSort.ACCURACY);
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("getBlogList.size: 50 이하여야 합니다");
    }

    @Test
    void getBlogs_sort_가_null_일때_에러() {
        Executable execute = () -> kakaoFeignClientAdapter.getBlogList("sample", null, null, null);
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("getBlogList.sort: 필수 값입니다");
    }
}