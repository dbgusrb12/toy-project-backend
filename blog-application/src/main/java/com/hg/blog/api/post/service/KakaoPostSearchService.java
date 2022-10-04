package com.hg.blog.api.post.service;

import com.hg.blog.api.post.dto.BlogType;
import com.hg.blog.api.post.dto.PostDto.GetPostList;
import com.hg.blog.domain.dto.DefaultPage;
import com.hg.blog.feign.kakao.KakaoFeignClientAdapter;
import com.hg.blog.feign.kakao.dto.KakaoBlog;
import com.hg.blog.feign.kakao.dto.KakaoBlog.Document;
import com.hg.blog.feign.kakao.dto.KakaoSort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoPostSearchService implements PostSearchService {

    private final KakaoFeignClientAdapter kakaoFeignClientAdapter;

    @Override
    public DefaultPage<GetPostList> getPosts(String search, int page, int size) {
        DefaultPage<Document> posts = this.getKakaoBlogResult(search, page, size);
        return posts.map(post ->
            new GetPostList(
                BlogType.KAKAO,
                null,
                post.getTitle(),
                post.getContents(),
                post.getBlogname(),
                post.getUrl(),
                post.getDatetime()
            )
        );
    }

    private DefaultPage<Document> getKakaoBlogResult(String search, int page, int size) {
        KakaoBlog blogList = kakaoFeignClientAdapter.getBlogList(search, page, size, KakaoSort.RECENCY);
        return new DefaultPage<>(
            blogList.getDocuments(),
            blogList.getMeta().getTotal_count(),
            blogList.getMeta().getPageable_count()
        );
    }
}
