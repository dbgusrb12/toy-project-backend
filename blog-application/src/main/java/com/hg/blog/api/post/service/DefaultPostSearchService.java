package com.hg.blog.api.post.service;

import com.hg.blog.api.post.dto.BlogType;
import com.hg.blog.api.post.dto.PostDto.GetPostList;
import com.hg.blog.domain.dto.DefaultPage;
import com.hg.blog.domain.keyword.service.KeywordCommandService;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Primary
public class DefaultPostSearchService implements PostSearchService {

    private final InAppPostSearchService inAppPostSearchService;
    private final NaverPostSearchService naverPostSearchService;
    private final KeywordCommandService keywordCommandService;

    @Override
    public DefaultPage<GetPostList> getPosts(BlogType blogType, String search, int page, int size) {
        addKeyword(search);
        switch (blogType) {
            case IN_APP:
                return this.getPosts(search, page, size);
            case NAVER:
                return naverPostSearchService.getPosts(search, page, size);
            case KAKAO:
                return new DefaultPage<>(new ArrayList<>(), 0, 0);
            default:
                throw new IllegalArgumentException("검색 불가능한 타입입니다.");
        }
    }

    @Override
    public DefaultPage<GetPostList> getPosts(String search, int page, int size) {
        return inAppPostSearchService.getPosts(search, page, size);
    }

    private void addKeyword(String search) {
        if (StringUtils.hasText(search)) {
            keywordCommandService.addKeywordEvent(search);
        }
    }
}
