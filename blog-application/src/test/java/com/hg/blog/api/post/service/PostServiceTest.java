package com.hg.blog.api.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.hg.blog.api.post.dto.PostDto.GetPost;
import com.hg.blog.api.post.dto.PostDto.PostCreateCommand;
import com.hg.blog.api.post.dto.PostDto.PostUpdateCommand;
import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.service.AccountQueryService;
import com.hg.blog.domain.dto.DefaultPage;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.service.PostCommandService;
import com.hg.blog.domain.post.service.PostQueryService;
import java.util.List;
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
    private PostQueryService postQueryService;
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
        given(accountQueryService.getAccountByUserId(userId))
            .willReturn(account);

        // when
        PostCreateCommand request = createPostCreateCommand();
        postService.savePost(userId, request);

        // then
        verify(postCommandService).savePost(account, title, content);
    }

    @Test
    public void updatePostTest() {
        // given
        Account account = createAccount();
        long postId = 1;
        PostUpdateCommand command = createPostUpdateCommand();
        given(accountQueryService.getAccountByUserId(userId))
            .willReturn(account);

        // when
        postService.updatePost(postId, userId, command);

        // then
        verify(postCommandService).updatePost(account, postId, command.getTitle(), command.getContent());
    }

    @Test
    public void deletePostTest() {
        // given
        Account account = createAccount();
        given(accountQueryService.getAccountByUserId(userId))
            .willReturn(account);

        // when
        long postId = 1;
        postService.deletePost(postId, userId);

        // then
        verify(postCommandService).deletePost(account, postId);
    }

    @Test
    public void getPostTest() {
        // given
        Account account = createAccount();
        Post post = createPost(account);
        long postId = 1;
        given(postQueryService.getPost(postId))
            .willReturn(post);

        // when
        GetPost getPost = postService.getPost(postId);

        // then
        assertThat(getPost).isNotNull();
        assertThat(getPost.getTitle()).isEqualTo(title);
        assertThat(getPost.getContent()).isEqualTo(content);
        assertThat(getPost.getNickname()).isEqualTo(account.getNickname());
    }

    @Test
    public void getPostsTest() {
        // given
        String search = "";
        int page = 0;
        int size = 5;
        Account account = createAccount();
        List<Post> content = createPosts(account);

        given(postQueryService.getPosts(search, page, size))
            .willReturn(new DefaultPage<>(content, 3, 1, 0));

        DefaultPage<GetPost> posts = postService.getPosts(search, page, size);
        assertThat(posts.getCurrentPage()).isEqualTo(0);
        assertThat(posts.getTotalElements()).isEqualTo(3);
        assertThat(posts.getTotalPages()).isEqualTo(1);
        assertThat(posts.getContent().size()).isEqualTo(3);
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

    private List<Post> createPosts(Account account) {
        return List.of(
            createPost(account),
            createPost(account),
            createPost(account)
        );
    }
}