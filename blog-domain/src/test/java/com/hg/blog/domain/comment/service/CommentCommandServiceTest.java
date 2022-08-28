package com.hg.blog.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.entity.AccountRepository;
import com.hg.blog.domain.comment.entity.Comment;
import com.hg.blog.domain.comment.entity.CommentRepository;
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

    private final String userId = "userId";
    private final String title = "post";
    private final String content = "content";

    @Test
    public void saveCommentTest() {
        // given
        Account account = saveAccount(userId);
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
        Account account = saveAccount(userId);
        Post post = savePost(account);
        Comment comment = commentCommandService.saveComment(account, post, content);

        // when
        Comment childComment = commentCommandService.saveChildComment(account, comment, content);

        // then
        assertThat(childComment).isNotNull();
        assertThat(childComment.getContent()).isEqualTo(content);
        assertThat(childComment.getPost()).isEqualTo(post);
        assertThat(childComment.getParentComment()).isEqualTo(comment);
    }

    @Test
    public void updateCommentTest() {
        // given
        Account account = saveAccount(userId);
        Post post = savePost(account);
        Comment comment = commentCommandService.saveComment(account, post, content);
        String updateContent = "update content";

        // when
        Comment updatedComment = commentCommandService.updateComment(account, comment.getId(), updateContent);

        // then
        assertThat(updatedComment).isNotNull();
        assertThat(updatedComment.getContent()).isEqualTo(updateContent);
    }

    @Test
    public void updateCommentTest_댓글이_존재하지_않을_때_에러() {
        // given
        Account account = saveAccount(userId);
        String updateContent = "update content";

        // when
        Executable execute = () -> commentCommandService.updateComment(account, 1L, updateContent);

        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 댓글입니다.");
    }

    @Test
    public void updateCommentTest_작성자가_아닐_경우_에러() {
        // given
        Account account = saveAccount(userId);
        Post post = savePost(account);
        Comment comment = commentCommandService.saveComment(account, post, content);
        String updateContent = "update content";
        Account otherAccount = saveAccount("otherUserId");

        // when
        Executable execute = () -> commentCommandService.updateComment(otherAccount, comment.getId(), updateContent);

        // then
        AccessControlException exception = assertThrows(AccessControlException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("권한이 없습니다.");
    }

    @Test
    public void deleteCommentTest() {
        // given
        Account account = saveAccount(userId);
        Post post = savePost(account);
        Comment comment = commentCommandService.saveComment(account, post, content);

        // when
        commentCommandService.deleteComment(account, comment.getId());

        // then
        Optional<Comment> deletedComment = commentRepository.findById(comment.getId());
        assertThat(deletedComment.isPresent()).isTrue();
        assertThat(deletedComment.get().isDeleted()).isTrue();
    }

    @Test
    public void deleteCommentTest_댓글이_존재하지_않을_때_에러() {
        // given
        Account account = saveAccount(userId);

        // when
        Executable execute = () -> commentCommandService.deleteComment(account, 1L);

        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 댓글입니다.");
    }

    @Test
    public void deleteCommentTest_작성자가_아닐_경우_에러() {
        // given
        Account account = saveAccount(userId);
        Post post = savePost(account);
        Comment comment = commentCommandService.saveComment(account, post, content);
        Account otherAccount = saveAccount("otherUserId");

        // when
        Executable execute = () -> commentCommandService.deleteComment(otherAccount, comment.getId());

        // then
        AccessControlException exception = assertThrows(AccessControlException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("권한이 없습니다.");
    }

    private Account saveAccount(String userId) {
        Account account = Account.of(userId, "password", "nickname");
        return accountRepository.save(account);
    }

    private Post savePost(Account account) {
        Post post = Post.of(account, title, content);
        return postRepository.save(post);
    }

}