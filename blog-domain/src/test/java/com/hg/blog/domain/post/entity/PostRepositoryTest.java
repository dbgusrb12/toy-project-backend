package com.hg.blog.domain.post.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.entity.AccountRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles({"blog-domain", "local"})
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountRepository accountRepository;

    private final String title = "post";
    private final String content = "content";

    @Test
    public void savePostTest() {
        // given
        Account account = saveAccount();

        // when
        Post post = savePost(account);

        // then
        assertThat(post).isNotNull();
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getAccount()).isEqualTo(account);
    }

    @Test
    public void findByIdAndDeletedTest() {
        // given
        Account account = saveAccount();
        Post post = savePost(account);

        // when
        Optional<Post> byId = postRepository.findByIdAndDeleted(post.getId(), false);

        // then
        assertThat(byId.isPresent()).isTrue();
        Post findPost = byId.get();
        assertThat(findPost.getTitle()).isEqualTo(title);
        assertThat(findPost.getContent()).isEqualTo(content);
        assertThat(findPost.getAccount()).isEqualTo(account);
    }

    @Test
    void findByContentContains() {
        // given
        Account account = saveAccount();
        savePost(account);
        savePost(account);
        savePost(account);

        // when
        Page<Post> posts = postRepository.findByContentContainsAndDeleted("content", false, PageRequest.of(0, 5));

        // then
        assertThat(posts.getTotalElements()).isEqualTo(3);
        assertThat(posts.getContent().size()).isEqualTo(3);
        assertThat(posts.getTotalPages()).isEqualTo(1);
    }

    @Test
    void findByList() {
        // given
        Account account = saveAccount();
        savePost(account);
        savePost(account);
        savePost(account);

        // when
        Page<Post> posts = postRepository.findByDeleted(false, PageRequest.of(0, 5));

        // then
        assertThat(posts.getTotalElements()).isEqualTo(3);
        assertThat(posts.getContent().size()).isEqualTo(3);
        assertThat(posts.getTotalPages()).isEqualTo(1);
    }

    private Account saveAccount() {
        Account account = Account.of("userId", "password", "nickname");
        return accountRepository.save(account);
    }

    private Post savePost(Account account) {
        Post post = Post.of(account, title, content);
        return postRepository.save(post);
    }
}