package io.f12.notionlinkedblog.api.comments;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.f12.notionlinkedblog.api.common.Endpoint;
import io.f12.notionlinkedblog.domain.comments.dto.CommentSearchDto;
import io.f12.notionlinkedblog.security.login.ajax.dto.LoginUser;
import io.f12.notionlinkedblog.service.comments.CommentsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoint.Api.POST)
public class CommentsApiController {

	private final CommentsService commentsService;
	private final String getParam = "/{id}";

	@GetMapping(getParam + Endpoint.Api.COMMENTS)
	public List<CommentSearchDto> getComments(@PathVariable("id") Long postId) {
		return commentsService.getCommentsByPostId(postId);
	}

	@PostMapping(getParam + Endpoint.Api.COMMENTS)
	@ResponseStatus(HttpStatus.CREATED)
	public CommentSearchDto createComments(@PathVariable("id") Long postId,
		@AuthenticationPrincipal LoginUser loginUser, @RequestBody Map<String, String> editMap) {
		return commentsService.createComments(postId, loginUser.getUser().getId(), editMap.get("comments"));
	}

	@PutMapping(getParam + Endpoint.Api.COMMENTS)
	@ResponseStatus(HttpStatus.CREATED)
	public CommentSearchDto editComments(@PathVariable("id") Long commentsId,
		@AuthenticationPrincipal LoginUser loginUser,
		@RequestBody Map<String, String> editMap) {
		return commentsService.editComment(commentsId, loginUser.getUser().getId(), editMap.get("comments"));
	}

	@DeleteMapping(getParam + Endpoint.Api.COMMENTS)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeComments(@PathVariable("id") Long commentId, @AuthenticationPrincipal LoginUser loginUser) {
		commentsService.removeComment(commentId, loginUser.getUser().getId());
	}

}
