package io.f12.notionlinkedblog.service.comments;

import static io.f12.notionlinkedblog.exceptions.ExceptionMessages.CommentExceptionsMessages.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.f12.notionlinkedblog.domain.comments.Comments;
import io.f12.notionlinkedblog.domain.comments.dto.CommentSearchDto;
import io.f12.notionlinkedblog.domain.comments.dto.CreateCommentDto;
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
			if (Comments.getParent() == null) {
				CommentSearchDto buildDto = CommentSearchDto.builder()
					.comments(Comments.getContent())
					.username(Comments.getUser().getUsername())
					.deep(Comments.getDeep())
					.build();
				returnDto.add(buildDto);
			} else {
				CommentSearchDto buildDto = CommentSearchDto.builder()
					.comments(Comments.getContent())
					.username(Comments.getUser().getUsername())
					.deep(Comments.getDeep())
					.parentCommentId(Comments.getParent().getId())
					.build();
				returnDto.add(buildDto);
			}
		});

		return returnDto;
	}

	public CommentSearchDto createComments(Long postId, Long userId, CreateCommentDto commentDto) {
		Post post = postDataRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.PostExceptionsMessages.POST_NOT_EXIST));
		User user = userDataRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.UserExceptionsMessages.USER_NOT_EXIST));

		Comments parentComment = null;
		if (commentDto.getDeep().equals(1)) {
			parentComment = commentsDataRepository.findById(commentDto.getParentCommentId())
				.orElseThrow(() -> new IllegalArgumentException(
					COMMENT_NOT_EXIST));
		}

		Comments builtComments = Comments.builder()
			.post(post)
			.content(commentDto.getComment())
			.user(user)
			.deep(commentDto.getDeep())
			.parent(parentComment)
			.build();

		Comments savedComments = commentsDataRepository.save(builtComments);

		CommentSearchDto builtReturnDto = CommentSearchDto.builder()
			.username(user.getUsername())
			.comments(savedComments.getContent())
			.deep(savedComments.getDeep())
			.build();

		if (parentComment != null) {
			parentComment.addChildren(savedComments);
			builtReturnDto.setParentCommentId(savedComments.getParent().getId());
		}
		return builtReturnDto;
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
		commentsDataRepository.deleteById(commentId);
	}

	private boolean isSameUser(Long sessionUserId, Long databaseUserId) {
		return Objects.equals(sessionUserId, databaseUserId);
	}
}
