package com.hg.blog.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.entity.AccountRepository;
import com.hg.blog.domain.comment.entity.Comment;
import com.hg.blog.domain.comment.entity.CommentRepository;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.entity.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(CommentCommandService.class)
@ActiveProfiles({"blog-domain", "local"})
class CommentCommandServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentCommandService commentCommandService;

    private final String title = "post";
    private final String content = "content";

    @Test
    public void saveCommentTest() {
        // given
        Account account = saveAccount();
        Post post = savePost(account);

        // when
        Comment comment = commentCommandService.saveComment(account, post, content);

        // then
        assertThat(comment).isNotNull();
        assertThat(comment.getContent()).isEqualTo(content);
        assertThat(comment.getPost()).isEqualTo(post);
    }

    @Test
    public void saveChildCommentTest() {
        // given
        Account account = saveAccount();
        Post post = savePost(account);
        Comment comment = commentCommandService.saveComment(account, post, content);

        // when
        Comment childComment = commentCommandService.saveComment(account, post, comment, content);

        // then
        assertThat(childComment).isNotNull();
        assertThat(childComment.getContent()).isEqualTo(content);
        assertThat(childComment.getPost()).isEqualTo(post);
        assertThat(childComment.getParentComment()).isEqualTo(comment);
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