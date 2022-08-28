package com.hg.blog.api.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.hg.blog.api.comment.dto.CommentDto.CommentCreateCommand;
import com.hg.blog.domain.account.entity.Account;
import com.hg.blog.domain.account.service.AccountQueryService;
import com.hg.blog.domain.comment.entity.Comment;
import com.hg.blog.domain.comment.service.CommentCommandService;
import com.hg.blog.domain.post.entity.Post;
import com.hg.blog.domain.post.service.PostQueryService;
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
        long id = commentService.saveComment(userId, command);

        // then
        assertThat(id).isEqualTo(0);
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


    private CommentCreateCommand createCommentCreateCommand() {
        return new CommentCreateCommand(1L, content);
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
}