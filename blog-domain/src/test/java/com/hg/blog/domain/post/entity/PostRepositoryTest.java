package com.hg.blog.domain.post.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.entity.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles({"blog-domain", "local"})
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountRepository accountRepository;

    private final String title = "post1";
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


    private Account saveAccount() {
        String userId = "userid";
        Account account = Account.of(userId, "password", "nickname");
        return accountRepository.save(account);
    }

    private Post savePost(Account account) {
        Post post = Post.of(account, title, content);
        return postRepository.save(post);
    }
}