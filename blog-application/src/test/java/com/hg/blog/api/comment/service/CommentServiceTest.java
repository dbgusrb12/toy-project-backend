package com.hg.blog.api.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

import com.hg.blog.api.comment.dto.CommentDto.ChildCommentCreateCommand;
import com.hg.blog.api.comment.dto.CommentDto.CommentCreateCommand;
import com.hg.blog.api.comment.dto.CommentDto.CommentUpdateCommand;
import com.hg.blog.api.comment.dto.CommentDto.GetComment;
import com.hg.blog.api.comment.dto.CommentType;
import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.service.AccountQueryService;
import com.hg.blog.domain.comment.entity.Comment;
import com.hg.blog.domain.comment.service.CommentCommandService;
import com.hg.blog.domain.comment.service.CommentQueryService;
import com.hg.blog.domain.dto.DefaultPage;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.service.PostQueryService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentCommandService commentCommandService;
    @Mock
    private CommentQueryService commentQueryService;
    @Mock
    private PostQueryService postQueryService;
    @Mock
    private AccountQueryService accountQueryService;
    @InjectMocks
    private CommentService commentService;
    private final String userId = "userId";
    private final String title = "post1";
    private final String content = "content";

    @Test
    public void saveCommentTest() {
        // given
        CommentCreateCommand command = createCommentCreateCommand();
        Account account = createAccount();
        Post post = createPost(account);
        Comment comment = createComment(account, post);
        given(accountQueryService.getAccountByUserId(userId))
            .willReturn(account);
        given(postQueryService.getPost(command.getPostId()))
            .willReturn(post);
        given(commentCommandService.saveComment(account, post, command.getContent()))
            .willReturn(comment);

        // when
        commentService.saveComment(userId, command);

        // then
        verify(commentCommandService).saveComment(account, post, command.getContent());
    }

    @Test
    public void saveCommentTest_유저가_존재하지_않을_경우_에러() {
        // given
        CommentCreateCommand command = createCommentCreateCommand();
        given(accountQueryService.getAccountByUserId(userId))
            .willThrow(new IllegalArgumentException("존재하지 않는 유저입니다."));

        // when
        Executable execute = () -> commentService.saveComment(userId, command);

        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 유저입니다.");
    }

    @Test
    public void saveCommentTest_게시글이_존재하지_않을_경우_에러() {
        // given
        CommentCreateCommand command = createCommentCreateCommand();
        Account account = createAccount();
        given(accountQueryService.getAccountByUserId(userId))
            .willReturn(account);
        given(postQueryService.getPost(command.getPostId()))
            .willThrow(new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // when
        Executable execute = () -> commentService.saveComment(userId, command);

        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 게시글입니다.");
    }

    @Test
    public void updateCommentTest() {
        // given
        long commentId = 1;
        CommentUpdateCommand command = createCommentUpdateCommand();
        Account account = createAccount();
        Post post = createPost(account);
        Comment comment = createComment(account, post);
        given(accountQueryService.getAccountByUserId(userId))
            .willReturn(account);
        given(commentCommandService.updateComment(account, commentId, command.getContent()))
            .willReturn(comment);

        // when
        commentService.updateComment(commentId, userId, command);

        // then
        verify(commentCommandService).updateComment(account, commentId, command.getContent());
    }

    @Test
    public void updateCommentTest_유저가_존재하지_않을_경우_에러() {
        // given
        long commentId = 1;
        CommentUpdateCommand command = createCommentUpdateCommand();
        given(accountQueryService.getAccountByUserId(userId))
            .willThrow(new IllegalArgumentException("존재하지 않는 유저입니다."));

        // when
        Executable execute = () -> commentService.updateComment(commentId, userId, command);

        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 유저입니다.");
    }

    @Test
    public void updateCommentTest_댓글이_존재하지_않을_경우_에러() {
        // given
        long commentId = 1;
        CommentUpdateCommand command = createCommentUpdateCommand();
        Account account = createAccount();
        given(accountQueryService.getAccountByUserId(userId))
            .willReturn(account);
        given(commentCommandService.updateComment(account, commentId, command.getContent()))
            .willThrow(new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // when
        Executable execute = () -> commentService.updateComment(commentId, userId, command);

        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 댓글입니다.");
    }

    @Test
    public void deleteCommentTest() {
        // given
        Account account = createAccount();
        given(accountQueryService.getAccountByUserId(userId))
            .willReturn(account);

        // when
        long commentId = 1;
        commentService.deleteComment(commentId, userId);

        // then
        verify(commentCommandService).deleteComment(account, commentId);
    }

    @Test
    public void deleteCommentTest_유저가_존재하지_않을_경우_에러() {
        // given
        given(accountQueryService.getAccountByUserId(userId))
            .willThrow(new IllegalArgumentException("존재하지 않는 유저입니다."));

        // when
        long commentId = 1;
        Executable execute = () -> commentService.deleteComment(commentId, userId);

        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 유저입니다.");
    }

    @Test
    public void deleteCommentTest_댓글이_존재하지_않을_경우_에러() {
        // given
        long commentId = 1;
        Account account = createAccount();
        given(accountQueryService.getAccountByUserId(userId))
            .willReturn(account);
        willThrow(new IllegalArgumentException("존재하지 않는 댓글입니다."))
            .given(commentCommandService).deleteComment(account, commentId);

        // when
        Executable execute = () -> commentService.deleteComment(commentId, userId);

        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 댓글입니다.");
    }

    @Test
    public void getCommentTest() {
        // given
        long commentId = 1;
        Account account = createAccount();
        Post post = createPost(account);
        Comment comment = createComment(account, post);
        given(commentQueryService.getComment(commentId))
            .willReturn(comment);

        // when
        GetComment getComment = commentService.getComment(commentId);

        // then
        assertThat(getComment).isNotNull();
        assertThat(getComment.getContent()).isEqualTo("content");
        assertThat(getComment.getNickname()).isEqualTo("nickname");
    }

    @Test
    public void getCommentTest_댓글이_존재하지_않을_경우_에러() {
        // given
        long commentId = 1;
        given(commentQueryService.getComment(commentId))
            .willThrow(new IllegalArgumentException("존재하지 않는 댓글입니다."));

        // when
        Executable execute = () -> commentService.getComment(commentId);

        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 댓글입니다.");
    }

    @Test
    public void saveChildCommentTest() {
        // given
        long commentId = 1;
        ChildCommentCreateCommand command = createChildCommentCreateCommand();
        Account account = createAccount();
        Post post = createPost(account);
        Comment comment = createComment(account, post);
        Comment childComment = createComment(account, post);
        given(accountQueryService.getAccountByUserId(userId))
            .willReturn(account);
        given(commentQueryService.getComment(commentId))
            .willReturn(comment);
        given(commentCommandService.saveChildComment(account, comment, command.getContent()))
            .willReturn(childComment);

        // when
        commentService.saveChildComment(commentId, userId, command);

        // then
        verify(commentCommandService).saveChildComment(account, comment, command.getContent());
    }

    @Test
    public void saveChildCommentTest_유저가_존재하지_않을_경우_에러() {
        // given
        long commentId = 1;
        ChildCommentCreateCommand command = createChildCommentCreateCommand();
        given(accountQueryService.getAccountByUserId(userId))
            .willThrow(new IllegalArgumentException("존재하지 않는 유저입니다."));

        // when
        Executable execute = () -> commentService.saveChildComment(commentId, userId, command);

        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 유저입니다.");
    }

    @Test
    public void saveChildCommentTest_댓글이_존재하지_않을_경우_에러() {
        // given
        long commentId = 1;
        ChildCommentCreateCommand command = createChildCommentCreateCommand();
        Account account = createAccount();
        given(accountQueryService.getAccountByUserId(userId))
            .willReturn(account);
        given(commentQueryService.getComment(commentId))
            .willThrow(new IllegalArgumentException("존재하지 않는 댓글입니다."));
        // when
        Executable execute = () -> commentService.saveChildComment(commentId, userId, command);

        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, execute);
        assertThat(exception.getMessage()).isEqualTo("존재하지 않는 댓글입니다.");
    }

    @Test
    public void getCommentsTest() {
        // given
        Account account = createAccount();
        Post post = createPost(account);
        List<Comment> content = createComments(account, post);
        long refId = 1;
        int page = 0;
        int size = 5;
        given(commentQueryService.getComments(refId, page, size))
            .willReturn(new DefaultPage<>(content, 3, 1, 0));

        // when
        DefaultPage<GetComment> comments = commentService.getComments(CommentType.ROOT, refId, page, size);

        // then
        assertThat(comments.getCurrentPage()).isEqualTo(0);
        assertThat(comments.getTotalElements()).isEqualTo(3);
        assertThat(comments.getTotalPages()).isEqualTo(1);
        assertThat(comments.getContent().size()).isEqualTo(3);
    }

    @Test
    public void getChildCommentsTest() {
        // given
        Account account = createAccount();
        Post post = createPost(account);
        List<Comment> content = createComments(account, post);
        long refId = 1;
        int page = 0;
        int size = 5;
        given(commentQueryService.getChildComments(refId, page, size))
            .willReturn(new DefaultPage<>(content, 3, 1, 0));

        // when
        DefaultPage<GetComment> comments = commentService.getComments(CommentType.CHILD, refId, page, size);

        // then
        assertThat(comments.getCurrentPage()).isEqualTo(0);
        assertThat(comments.getTotalElements()).isEqualTo(3);
        assertThat(comments.getTotalPages()).isEqualTo(1);
        assertThat(comments.getContent().size()).isEqualTo(3);
    }

    private ChildCommentCreateCommand createChildCommentCreateCommand() {
        return new ChildCommentCreateCommand("comment content");
    }

    private CommentCreateCommand createCommentCreateCommand() {
        return new CommentCreateCommand(1L, "comment content");
    }

    private CommentUpdateCommand createCommentUpdateCommand() {
        return new CommentUpdateCommand("update comment content");
    }

    private Account createAccount() {
        return Account.of(userId, "password", "nickname");
    }

    private Post createPost(Account account) {
        return Post.of(account, title, content);
    }

    private Comment createComment(Account account, Post post) {
        return Comment.of(account, post, content);
    }

    private List<Comment> createComments(Account account, Post post) {
        return List.of(
            createComment(account, post),
            createComment(account, post),
            createComment(account, post)
        );
    }
}