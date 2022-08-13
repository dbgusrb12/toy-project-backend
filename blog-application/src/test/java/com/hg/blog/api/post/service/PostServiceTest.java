package com.hg.blog.api.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.hg.blog.api.post.dto.PostDto.PostCreateCommand;
import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.service.PostCommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostCommandService postCommandService;

    @InjectMocks
    private PostService postService;
    private final String userId = "userId";
    private final String title = "post1";
    private final String content = "content";

    @Test
    public void savePostTest() {
        // given
        Account account = createAccount();
        Post post = createPost(account);
        given(postCommandService.savePost(userId, title, content))
            .willReturn(post);

        PostCreateCommand request = createPostCreateCommand();
        // when
        long id = postService.savePost(userId, request);
        // then
        assertThat(id).isEqualTo(0);
    }

    private PostCreateCommand createPostCreateCommand() {
        return new PostCreateCommand(title, content);
    }

    private Account createAccount() {
        return Account.of(userId, "password", "nickname");
    }

    private Post createPost(Account account) {
        return Post.of(account, title, content);
    }
}