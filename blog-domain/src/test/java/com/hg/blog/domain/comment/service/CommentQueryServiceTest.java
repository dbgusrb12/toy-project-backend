package com.hg.blog.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.entity.AccountRepository;
import com.hg.blog.domain.comment.entity.Comment;
import com.hg.blog.domain.comment.entity.CommentRepository;
import com.hg.blog.domain.dto.DefaultPage;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.entity.PostRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(CommentQueryService.class)
@ActiveProfiles({"blog-domain", "local"})
class CommentQueryServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentQueryService commentQueryService;

    private final String content = "content";
    private long postId;
    private long commentId;
    private long childCommentId;
    private long lastCommentId;

    @BeforeEach
    void setUp() {
        Account account = saveAccount();
        Post post = savePost(account);
        Comment comment = saveComment(account, post);
        Comment childComment = saveChildComment(account, post, comment);
        Comment lastComment = saveChildComment(account, post, childComment);
        this.postId = post.getId();
        this.commentId = comment.getId();
        this.childCommentId = childComment.getId();
        this.lastCommentId = lastComment.getId();
    }

    @Test
    void getCommentTest_상위_댓글_존재() {
        // given, when
        Comment comment = commentQueryService.getComment(childCommentId);

        // then
        assertThat(comment).isNotNull();
        assertThat(comment.getParentComment().getId()).isEqualTo(commentId);
        assertThat(comment.getContent()).isEqualTo(content);
    }

    @Test
    void getCommentTest_하위_댓글_존재() {
        // given, when
        Comment comment = commentQueryService.getComment(childCommentId);

        // then
        assertThat(comment).isNotNull();
        List<Comment> childComment = comment.getChildComment();
        assertThat(childComment.size()).isEqualTo(1);
        assertThat(childComment.get(0).getId()).isEqualTo(lastCommentId);
    }

    @Test
    void getCommentsTest() {
        DefaultPage<Comment> comments = commentQueryService.getComments(postId, 0, 5);

        assertThat(comments.getTotalElements()).isEqualTo(1);
        assertThat(comments.getTotalPages()).isEqualTo(1);
        assertThat(comments.getCurrentPage()).isEqualTo(0);
        assertThat(comments.getContent().size()).isEqualTo(1);
    }

    @Test
    void getChildCommentsTest() {
        DefaultPage<Comment> comments = commentQueryService.getChildComments(commentId, 0, 5);

        assertThat(comments.getTotalElements()).isEqualTo(1);
        assertThat(comments.getTotalPages()).isEqualTo(1);
        assertThat(comments.getCurrentPage()).isEqualTo(0);
        assertThat(comments.getContent().size()).isEqualTo(1);
    }


    private Account saveAccount() {
        Account account = Account.of("userId", "password", "nickname");
        return accountRepository.save(account);
    }

    private Post savePost(Account account) {
        Post post = Post.of(account, "post", "content");
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