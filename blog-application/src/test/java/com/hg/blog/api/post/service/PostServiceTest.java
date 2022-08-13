package com.hg.blog.api.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.hg.blog.api.post.dto.PostDto.PostCreateCommand;
import com.hg.blog.api.post.dto.PostDto.PostUpdateCommand;
import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.service.AccountQueryService;
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
    @Mock
    private AccountQueryService accountQueryService;

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
        given(accountQueryService.getAccountByUserId(userId))
            .willReturn(account);
        given(postCommandService.savePost(account, title, content))
            .willReturn(post);

        PostCreateCommand request = createPostCreateCommand();
        // when
        long id = postService.savePost(userId, request);
        // then
        assertThat(id).isEqualTo(0);
    }

    @Test
    public void updatePostTest() {
        Account account = createAccount();
        given(accountQueryService.getAccountByUserId(userId))
            .willReturn(account);
        Post post = createPost(account);
        long postId = 1;
        PostUpdateCommand command = createPostUpdateCommand();
        given(postCommandService.updatePost(account, postId, command.getTitle(),
            command.getContent())).willReturn(post);
        long id = postService.updatePost(postId, userId, command);
        assertThat(id).isEqualTo(0);
    }

    @Test
    public void deletePostTest() {
        Account account = createAccount();
        given(accountQueryService.getAccountByUserId(userId))
            .willReturn(account);
        long postId = 1;
        postService.deletePost(postId, userId);

        verify(postCommandService).deletePost(account, postId);
    }

    private PostCreateCommand createPostCreateCommand() {
        return new PostCreateCommand(title, content);
    }

    private PostUpdateCommand createPostUpdateCommand() {
        return new PostUpdateCommand(title, content);
    }

    private Account createAccount() {
        return Account.of(userId, "password", "nickname");
    }

    private Post createPost(Account account) {
        return Post.of(account, title, content);
    }
}