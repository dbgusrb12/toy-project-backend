package com.hg.blog.api.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.hg.blog.api.post.dto.PostDto.GetPostList;
import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.dto.DefaultPage;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.service.PostQueryService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InAppPostSearchServiceTest {

    @Mock
    private PostQueryService postQueryService;
    @InjectMocks
    private InAppPostSearchService inAppPostSearchService;
    private final String userId = "userId";
    private final String title = "post1";
    private final String content = "content";

    @Test
    public void getPostsTest() {
        // given
        String search = "";
        int page = 0;
        int size = 5;
        Account account = createAccount();
        List<Post> content = createPosts(account);
        given(postQueryService.getPosts(search, page, size))
            .willReturn(new DefaultPage<>(content, 3, 1));

        // when
        DefaultPage<GetPostList> posts = inAppPostSearchService.getPosts(search, page, size);

        // then
        assertThat(posts.getTotalElements()).isEqualTo(3);
        assertThat(posts.getTotalPages()).isEqualTo(1);
        assertThat(posts.getContent().size()).isEqualTo(3);
    }

    private List<Post> createPosts(Account account) {
        return List.of(
            createPost(account),
            createPost(account),
            createPost(account)
        );
    }

    private Account createAccount() {
        return Account.of(userId, "password", "nickname");
    }

    private Post createPost(Account account) {
        return Post.of(account, title, content);
    }

}