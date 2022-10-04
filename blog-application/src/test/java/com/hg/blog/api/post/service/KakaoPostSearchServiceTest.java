package com.hg.blog.api.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.hg.blog.api.post.dto.PostDto.GetPostList;
import com.hg.blog.domain.dto.DefaultPage;
import com.hg.blog.feign.kakao.KakaoFeignClientAdapter;
import com.hg.blog.feign.kakao.dto.KakaoBlog;
import com.hg.blog.feign.kakao.dto.KakaoBlog.Document;
import com.hg.blog.feign.kakao.dto.KakaoBlog.Meta;
import com.hg.blog.feign.kakao.dto.KakaoSort;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KakaoPostSearchServiceTest {

    @Mock
    KakaoFeignClientAdapter kakaoFeignClientAdapter;

    @InjectMocks
    KakaoPostSearchService kakaoPostSearchService;

    @Test
    void getPostsTest() {
        String search = "searchText";
        int page = 1;
        int size = 10;
        KakaoBlog kakaoBlog = getBlogResult();
        given(kakaoFeignClientAdapter.getBlogList(search, page, size, KakaoSort.RECENCY))
            .willReturn(kakaoBlog);

        DefaultPage<GetPostList> posts = kakaoPostSearchService.getPosts(search, page, size);

        assertThat(posts.getTotalElements()).isEqualTo(3);
        assertThat(posts.getTotalPages()).isEqualTo(1);
        assertThat(posts.getContent().size()).isEqualTo(3);
    }

    private Document getDocument() {
        return new Document(
            "title",
            "content",
            "url",
            "blogname",
            "thumbnail",
            LocalDateTime.now()
        );
    }

    private KakaoBlog getBlogResult() {
        List<Document> documents = List.of(
            getDocument(),
            getDocument(),
            getDocument()
        );
        Meta meta = new Meta(
            3,
            1,
            true
        );

        return new KakaoBlog(meta, documents);
    }
}