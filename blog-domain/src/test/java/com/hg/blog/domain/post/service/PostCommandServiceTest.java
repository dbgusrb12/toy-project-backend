package com.hg.blog.domain.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.entity.AccountRepository;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.entity.PostRepository;
import java.security.AccessControlException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(PostCommandService.class)
@ActiveProfiles({"blog-domain", "local"})
public class PostCommandServiceTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PostCommandService postCommandService;
    private final String userId = "userId";
    private final String title = "post";
    private final String content = "content";

    @Test
    public void savePostTest() {
        // given
        Account account = createAccount(userId);

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
        // given
        Account account = createAccount(userId);
        Post post = postCommandService.savePost(account, title, content);

        String updateTitle = "postUpdate";
        String updateContent = "content 수정";

        // when
        Post result = postCommandService.updatePost(account, post.getId(), updateTitle, updateContent);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(updateTitle);
        assertThat(result.getContent()).isEqualTo(updateContent);
    }

    @Test
    public void updatePostNotExistPostErrorTest() {
        // given
        Account account = createAccount(userId);
        long postId = 1;
        String updateTitle = "postUpdate";
        String updateContent = "content 수정";

        // when
        Executable execute = () -> postCommandService.updatePost(account, postId, updateTitle, updateContent);

        // then
        assertThrows(IllegalArgumentException.class, execute);
    }

    @Test
    public void updatePostNotOwnerErrorTest() {
        // given
        Account account = createAccount(userId);
        Post post = postCommandService.savePost(account, title, content);
        String updateTitle = "postUpdate";
        String updateContent = "content 수정";

        // when
        Account otherAccount = createAccount("otherUserId");
        Executable execute = () -> postCommandService.updatePost(
            otherAccount,
            post.getId(),
            updateTitle,
            updateContent
        );

        // then
        assertThrows(AccessControlException.class, execute);
    }

    @Test
    public void deletePostTest() {
        // given
        Account account = createAccount(userId);
        Post post = postCommandService.savePost(account, title, content);

        // when
        postCommandService.deletePost(account, post.getId());

        // then
        Optional<Post> deletedPost = postRepository.findById(post.getId());
        assertThat(deletedPost.isPresent()).isTrue();
        assertThat(deletedPost.get().isDeleted()).isTrue();
    }

    @Test
    public void deletePostNotExistPostErrorTest() {
        // given
        Account account = createAccount(userId);
        long postId = 1;

        // when
        Executable execute = () -> postCommandService.deletePost(account, postId);

        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 게시글입니다.");
    }

    @Test
    public void deletePostNotOwnerErrorTest() {
        // given
        Account account = createAccount(userId);
        Post post = postCommandService.savePost(account, title, content);

        // when
        Account otherAccount = createAccount("otherUserId");
        Executable execute = () -> postCommandService.deletePost(otherAccount, post.getId());

        // then
        AccessControlException exception = assertThrows(AccessControlException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("권한이 없습니다.");
    }

    private Account createAccount(String userId) {
        Account account = Account.of(userId, "password", "nickname");
        return accountRepository.save(account);
    }
}