package io.f12.notionlinkedblog.service.comments;

import static io.f12.notionlinkedblog.exceptions.ExceptionMessages.CommentExceptionsMessages.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.f12.notionlinkedblog.domain.comments.Comments;
import io.f12.notionlinkedblog.domain.comments.dto.CommentSearchDto;
import io.f12.notionlinkedblog.domain.post.Post;
import io.f12.notionlinkedblog.domain.user.User;
import io.f12.notionlinkedblog.exceptions.ExceptionMessages;
import io.f12.notionlinkedblog.repository.comments.CommentsDataRepository;
import io.f12.notionlinkedblog.repository.post.PostDataRepository;
import io.f12.notionlinkedblog.repository.user.UserDataRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentsService {

	private final CommentsDataRepository commentsDataRepository;
	private final PostDataRepository postDataRepository;
	private final UserDataRepository userDataRepository;

	public List<CommentSearchDto> getCommentsByPostId(Long postId) {
		List<Comments> byPostId = commentsDataRepository.findByPostId(postId);
		List<CommentSearchDto> returnDto = new ArrayList<>();

		byPostId.stream().iterator().forEachRemaining(Comments -> {
			CommentSearchDto buildDto = CommentSearchDto.builder()
				.comments(Comments.getContent())
				.username(Comments.getUser().getUsername())
				.build();
			returnDto.add(buildDto);
		});

		return returnDto;
	}

	public CommentSearchDto createComments(Long postId, Long userId, String content) {
		Post post = postDataRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.PostExceptionsMessages.POST_NOT_EXIST));
		User user = userDataRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.UserExceptionsMessages.USER_NOT_EXIST));

		Comments comments = Comments.builder()
			.post(post)
			.content(content)
			.user(user)
			.build();
		Comments savedComments = commentsDataRepository.save(comments);

		return CommentSearchDto.builder()
			.username(user.getUsername())
			.comments(savedComments.getContent())
			.build();
	}

	public CommentSearchDto editComment(Long commentId, Long userId, String contents) {
		Comments comments = commentsDataRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException(COMMENT_NOT_EXIST));

		if (!isSameUser(userId, comments.getUser().getId())) {
			throw new IllegalStateException(NOT_COMMENT_OWNER);
		}
		comments.editComments(contents);
		return CommentSearchDto.builder()
			.username(comments.getUser().getUsername())
			.comments(comments.getContent())
			.build();
	}

	public void removeComment(Long commentId, Long userId) {
		Comments comments = commentsDataRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException(COMMENT_NOT_EXIST));
		if (!isSameUser(userId, comments.getUser().getId())) {
			throw new IllegalStateException(NOT_COMMENT_OWNER);
		}
		commentsDataRepository.removeByIdAndUserId(commentId, userId);
	}

	private boolean isSameUser(Long sessionUserId, Long databaseUserId) {
		return Objects.equals(sessionUserId, databaseUserId);
	}
}
