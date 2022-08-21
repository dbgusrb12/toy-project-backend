package com.hg.blog.feign.naver;

import com.hg.blog.feign.naver.dto.BlogResult;
import com.hg.blog.feign.naver.dto.NaverSort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {"env=test", "feign.client.config.default.loggerLevel=FULL"})
@ActiveProfiles({"naver-client", "local"})
class NaverFeignClientAdapterTest {

    @Autowired
    private NaverFeignClientAdapter naverFeignClientAdapter;

    @Test
    void getBlogsTest() {
        BlogResult sample = naverFeignClientAdapter.getBlogList("sample", null, null, NaverSort.SIM);
        System.out.println(sample);
    }
}