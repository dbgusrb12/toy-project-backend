package com.hg.blog.domain.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.entity.PostRepository;
import java.security.AccessControlException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostCommandServiceTest {

    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private PostCommandService postCommandService;
    private final String userId = "userId";
    private final String title = "post";
    private final String content = "content";

    @Test
    public void savePostTest() {
        // given
        Account account = createAccount();
        given(postRepository.save(any()))
            .willReturn(createPost(account));
        // when
        Post post = postCommandService.savePost(account, title, content);
        // then
        assertThat(post).isNotNull();
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getAccount()).isEqualTo(account);
    }

    @Test
    public void updatePostTest() {
        Account account = createAccount();
        long postId = 1;
        Post post = Post.of(account, title, content);
        given(postRepository.findById(postId))
            .willReturn(Optional.of(post));
        String updateTitle = "postUpdate";
        String updateContent = "content 수정";
        post.updatePost(updateTitle, updateContent);
        given(postRepository.save(any()))
            .willReturn(post);

        Post result = postCommandService.updatePost(account, postId, updateTitle, updateContent);
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(updateTitle);
        assertThat(result.getContent()).isEqualTo(updateContent);
    }

    @Test
    public void updatePostNotExistPostErrorTest() {
        Account account = createAccount();
        long postId = 1;
        String updateTitle = "postUpdate";
        String updateContent = "content 수정";
        Executable execute = () -> postCommandService.updatePost(account, postId, updateTitle,
            updateContent);

        assertThrows(IllegalArgumentException.class, execute);
    }

    @Test
    public void updatePostNotOwnerErrorTest() {
        Account account = createAccount();
        Account otherAccount = createAccount();
        long postId = 1;
        Post post = Post.of(account, title, content);
        given(postRepository.findById(postId))
            .willReturn(Optional.of(post));
        String updateTitle = "postUpdate";
        String updateContent = "content 수정";
        Executable execute = () -> postCommandService.updatePost(otherAccount, postId, updateTitle,
            updateContent);

        assertThrows(AccessControlException.class, execute);
    }

    @Test
    public void deletePostTest() {
        Account account = createAccount();
        long postId = 1;
        Post post = Post.of(account, title, content);
        given(postRepository.findById(postId))
            .willReturn(Optional.of(post));
        postCommandService.deletePost(account, postId);
        verify(postRepository).save(any());
    }

    @Test
    public void deletePostNotExistPostErrorTest() {
        Account account = createAccount();
        long postId = 1;
        Executable execute = () -> postCommandService.deletePost(account, postId);

        assertThrows(IllegalArgumentException.class, execute);
    }

    @Test
    public void deletePostNotOwnerErrorTest() {
        Account account = createAccount();
        Account otherAccount = createAccount();
        long postId = 1;
        Post post = Post.of(account, title, content);
        given(postRepository.findById(postId))
            .willReturn(Optional.of(post));
        Executable execute = () -> postCommandService.deletePost(otherAccount, postId);

        assertThrows(AccessControlException.class, execute);
    }

    private Account createAccount() {
        return Account.of(userId, "password", "nickname");
    }

    private Post createPost(Account account) {
        return Post.of(account, title, content);
    }
}