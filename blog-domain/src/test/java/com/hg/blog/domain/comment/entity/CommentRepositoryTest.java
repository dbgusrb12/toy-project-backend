package com.hg.blog.domain.comment.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.entity.AccountRepository;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.entity.PostRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles({"blog-domain", "local"})
class CommentRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CommentRepository commentRepository;
    private final String content = "content";

    @Test
    void saveCommentTest() {
        // given
        Account account = saveAccount();
        Post post = savePost(account);

        // when
        Comment comment = saveComment(account, post);

        // then
        assertThat(comment).isNotNull();
        assertThat(comment.getContent()).isEqualTo(content);
        assertThat(comment.getPost()).isEqualTo(post);
    }

    @Test
    void saveChildCommentTest() {
        // given
        Account account = saveAccount();
        Post post = savePost(account);
        Comment comment = saveComment(account, post);

        // when
        Comment childComment = saveChildComment(account, post, comment);

        // then
        assertThat(childComment).isNotNull();
        assertThat(childComment.getContent()).isEqualTo(content);
        assertThat(childComment.getPost()).isEqualTo(post);
        assertThat(childComment.getParentComment()).isEqualTo(comment);
    }

    @Test
    void findByIdTest() {
        // given
        Account account = saveAccount();
        Post post = savePost(account);
        Comment comment = saveComment(account, post);

        // when
        Optional<Comment> byId = commentRepository.findById(comment.getId());

        // then
        assertThat(byId.isPresent()).isTrue();
        Comment findComment = byId.get();
        assertThat(findComment).isNotNull();
        assertThat(findComment.getContent()).isEqualTo(content);
        assertThat(findComment.getPost()).isEqualTo(post);
    }

    @Test
    void findByIdChildTest() {
        // given
        Account account = saveAccount();
        Post post = savePost(account);
        Comment comment = saveComment(account, post);
        Comment childComment = saveChildComment(account, post, comment);

        // when
        Optional<Comment> byId = commentRepository.findById(childComment.getId());

        // then
        assertThat(byId.isPresent()).isTrue();
        Comment findComment = byId.get();
        assertThat(findComment).isNotNull();
        assertThat(findComment.getContent()).isEqualTo(content);
        assertThat(findComment.getPost()).isEqualTo(post);
        assertThat(findComment.getParentComment()).isEqualTo(comment);
    }

    private Account saveAccount() {
        Account account = Account.of("userId", "password", "nickname");
        return accountRepository.save(account);
    }

    private Post savePost(Account account) {
        Post post = Post.of(account, "post", content);
        return postRepository.save(post);
    }

    private Comment saveComment(Account account, Post post) {
        Comment comment = Comment.of(account, post, content);
        return commentRepository.save(comment);
    }

    private Comment saveChildComment(Account account, Post post, Comment parentComment) {
        Comment comment = Comment.of(account, post, content);
        parentComment.addComment(comment);
        return commentRepository.save(comment);
    }
}