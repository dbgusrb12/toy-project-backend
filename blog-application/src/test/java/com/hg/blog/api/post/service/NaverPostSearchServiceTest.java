package com.hg.blog.api.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.hg.blog.api.post.dto.PostDto.GetPostList;
import com.hg.blog.domain.dto.DefaultPage;
import com.hg.blog.feign.naver.NaverFeignClientAdapter;
import com.hg.blog.feign.naver.dto.BlogResult;
import com.hg.blog.feign.naver.dto.BlogResult.Item;
import com.hg.blog.feign.naver.dto.NaverSort;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NaverPostSearchServiceTest {

    @Mock
    private NaverFeignClientAdapter naverFeignClientAdapter;

    @InjectMocks
    private NaverPostSearchService naverPostSearchService;

    @Test
    public void getPostsTest() {
        // given
        String search = "search text";
        int page = 0;
        int size = 10;
        BlogResult blogResult = getBlogResult();
        int start = 1;
        given(naverFeignClientAdapter.getBlogList(search, start, size, NaverSort.DATE))
            .willReturn(blogResult);

        // when
        DefaultPage<GetPostList> posts = naverPostSearchService.getPosts(search, page, size);

        // then
        assertThat(posts.getTotalElements()).isEqualTo(3);
        assertThat(posts.getTotalPages()).isEqualTo(1);
        assertThat(posts.getContent().size()).isEqualTo(3);
    }

    private Item getItem() {
        return new Item(
            "title",
            "link",
            "description",
            "bloggerName",
            "bloggerLink",
            LocalDate.now()
        );
    }

    private BlogResult getBlogResult() {
        List<Item> items = List.of(
            getItem(),
            getItem(),
            getItem()
        );
        return new BlogResult(LocalDateTime.now(), 3, 1, 3, items);
    }

}