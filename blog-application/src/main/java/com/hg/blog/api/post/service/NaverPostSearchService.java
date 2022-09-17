package com.hg.blog.api.post.service;

import com.hg.blog.api.post.dto.BlogType;
import com.hg.blog.api.post.dto.PostDto.GetPostList;
import com.hg.blog.domain.dto.DefaultPage;
import com.hg.blog.feign.naver.NaverFeignClientAdapter;
import com.hg.blog.feign.naver.dto.BlogResult;
import com.hg.blog.feign.naver.dto.BlogResult.Item;
import com.hg.blog.feign.naver.dto.NaverSort;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NaverPostSearchService implements PostSearchService {

    private final NaverFeignClientAdapter naverFeignClientAdapter;

    @Override
    public DefaultPage<GetPostList> getPosts(String search, int page, int size) {
        DefaultPage<Item> posts = this.getNaverBlogResult(search, page, size);
        return posts.map(post ->
            new GetPostList(
                BlogType.NAVER,
                null,
                post.getTitle(),
                post.getDescription(),
                post.getBloggername(),
                post.getLink(),
                post.getPostdate().atStartOfDay()
            )
        );
    }

    private DefaultPage<Item> getNaverBlogResult(String search, int page, int size) {
        BlogResult blogList;
        try {
            int start = page * size + 1;
            blogList = naverFeignClientAdapter.getBlogList(search, start, size, NaverSort.DATE);
        } catch (Exception e) {
            blogList = new BlogResult(LocalDateTime.now(), 0, 1, 0, new ArrayList<>());
        }
        long totalPage = blogList.getTotal() % size == 0
            ? blogList.getTotal() / size
            : blogList.getTotal() / size + 1;
        return new DefaultPage<>(
            blogList.getItems(),
            blogList.getTotal(),
            totalPage
        );
    }
}
