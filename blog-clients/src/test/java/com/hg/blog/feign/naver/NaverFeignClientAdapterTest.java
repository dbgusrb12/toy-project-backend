package com.hg.blog.feign.naver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.hg.blog.feign.kakao.dto.KakaoSort;
import com.hg.blog.feign.naver.dto.NaverBlog;
import com.hg.blog.feign.naver.dto.NaverSort;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {"env=test", "feign.client.config.default.loggerLevel=FULL"})
@ActiveProfiles({"blog-client", "local"})
class NaverFeignClientAdapterTest {

    @Autowired
    private NaverFeignClientAdapter naverFeignClientAdapter;

    @Test
    void getBlogsTest() {
        NaverBlog sample = naverFeignClientAdapter.getBlogList("sample", null, null, NaverSort.SIM);
        assertThat(sample.getItems().size()).isEqualTo(10);
    }

    @Test
    void getBlogs_query_가_존재하지_않을_때_에러() {
        Executable execute = () -> naverFeignClientAdapter.getBlogList("", null, null, NaverSort.SIM);
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("getBlogList.query: 필수 값입니다");
    }

    @Test
    void getBlogs_start_가_1_이하_일_때_에러() {
        Executable execute = () -> naverFeignClientAdapter.getBlogList("sample", 0, null, NaverSort.SIM);
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("getBlogList.start: 1 이상이어야 합니다");
    }

    @Test
    void getBlogs_start_가_1000_이상_일_때_에러() {
        Executable execute = () -> naverFeignClientAdapter.getBlogList("sample", 1001, null, NaverSort.SIM);
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("getBlogList.start: 1000 이하여야 합니다");
    }

    @Test
    void getBlogs_display_가_1_이하_일_때_에러() {
        Executable execute = () -> naverFeignClientAdapter.getBlogList("sample", null, 0, NaverSort.SIM);
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("getBlogList.display: 1 이상이어야 합니다");
    }

    @Test
    void getBlogs_display_가_100_이상_일_때_에러() {
        Executable execute = () -> naverFeignClientAdapter.getBlogList("sample", null, 101, NaverSort.SIM);
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("getBlogList.display: 100 이하여야 합니다");
    }

    @Test
    void getBlogs_sort_가_null_일때_에러() {
        Executable execute = () -> naverFeignClientAdapter.getBlogList("sample", null, null, null);
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("getBlogList.sort: 필수 값입니다");
    }
}