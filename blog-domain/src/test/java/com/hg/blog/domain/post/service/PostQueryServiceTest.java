package com.hg.blog.domain.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.entity.PostRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostQueryServiceTest {

    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private PostQueryService postQueryService;
    private final String title = "post";
    private final String content = "content";

    @Test
    public void getPostTest() {
        long postId = 1;
        Account account = createAccount();
        Post post = createPost(account);
        given(postRepository.findByIdAndDeleted(postId, false))
            .willReturn(Optional.of(post));

        Post getPost = postQueryService.getPost(postId);
        assertThat(getPost).isNotNull();
        assertThat(getPost.getTitle()).isEqualTo(title);
        assertThat(getPost.getContent()).isEqualTo(content);
        assertThat(getPost.getAccount()).isEqualTo(account);
    }

    @Test
    public void getPostNotExistError() {
        long postId = 1;
        Executable execute = () -> postQueryService.getPost(postId);
        assertThrows(IllegalArgumentException.class, execute);
    }

    private Account createAccount() {
        String userId = "userId";
        return Account.of(userId, "password", "nickname");
    }

    private Post createPost(Account account) {
        return Post.of(account, title, content);
    }

}