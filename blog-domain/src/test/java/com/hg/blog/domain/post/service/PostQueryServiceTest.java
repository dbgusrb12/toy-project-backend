package com.hg.blog.domain.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.entity.AccountRepository;
import com.hg.blog.domain.dto.DefaultPage;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.entity.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(PostQueryService.class)
@ActiveProfiles({"blog-domain", "local"})
public class PostQueryServiceTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PostQueryService postQueryService;
    private long postId;
    private Account account;

    @BeforeEach
    void setUp() {
        Account account = saveAccount();
        Post post = savePost(account);
        this.account = account;
        this.postId = post.getId();
    }

    @Test
    public void getPostTest() {
        // given, when
        Post getPost = postQueryService.getPost(postId);

        // then
        assertThat(getPost).isNotNull();
        assertThat(getPost.getTitle()).isEqualTo("post");
        assertThat(getPost.getContent()).isEqualTo("content");
        assertThat(getPost.getAccount()).isEqualTo(account);
        assertThat(getPost.getAccount().getUserId()).isEqualTo("userId");
        assertThat(getPost.getAccount().getNickname()).isEqualTo("nickname");
    }

    @Test
    public void getPostTest_게시글이_존재하지_않을_경우_에러() {
        // given
        long postId = 0;

        // when
        Executable execute = () -> postQueryService.getPost(postId);

        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 게시글입니다.");
    }

    @Test
    public void getPostsTest() {
        // given, when
        DefaultPage<Post> posts = postQueryService.getPosts(null, 0, 5);

        // then
        assertThat(posts.getTotalElements()).isEqualTo(1);
        assertThat(posts.getTotalPages()).isEqualTo(1);
        assertThat(posts.getCurrentPage()).isEqualTo(0);
        assertThat(posts.getContent().size()).isEqualTo(1);
    }

    private Account saveAccount() {
        Account account = Account.of("userId", "password", "nickname");
        return accountRepository.save(account);
    }

    private Post savePost(Account account) {
        Post post = Post.of(account, "post", "content");
        return postRepository.save(post);
    }

}