package io.f12.notionlinkedblog.api.post;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.f12.notionlinkedblog.domain.post.Post;
import io.f12.notionlinkedblog.domain.post.dto.PostCreateDto;
import io.f12.notionlinkedblog.domain.post.dto.PostEditDto;
import io.f12.notionlinkedblog.domain.post.dto.PostSearchDto;
import io.f12.notionlinkedblog.domain.user.dto.info.UserSearchDto;
import io.f12.notionlinkedblog.service.post.PostService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostApiController {

	private final PostService postService;

	//TODO: 현재 Series 기능 미포함
	@PutMapping
	public LocalDateTime createPost(HttpSession session, @RequestBody PostCreateDto postCreateDto) {
		UserSearchDto user = getUserFromSession(session);

		Post post = postService.createPost(user.getId(), postCreateDto.getTitle(), postCreateDto.getContent(),
			postCreateDto.getThumbnail());

		return post.getCreatedAt();
	}

	@GetMapping("/{id}")
	public PostSearchDto getPostsById(@PathVariable("id") Long id) {
		return postService.getPostDtoById(id);
	}

	//TODO: 포스트를 특정 조건에 맞춰서 가져오기.

	// public List<PostSearchDto> getPosts() {
	//
	// }

	//TODO: Series 기능과 함께 다듬어야 함
	@PutMapping("/{id}")
	public void editPost(@PathVariable("id") Long postId, HttpSession session, @RequestBody PostEditDto editInfo) {
		UserSearchDto user = getUserFromSession(session);
		postService.editPost(user.getId(), editInfo.getTitle(), editInfo.getContent(), editInfo.getThumbnail());
	}

	@DeleteMapping("/{id}")
	public void deletePost(@PathVariable("id") Long postId, HttpSession session) {
		UserSearchDto user = getUserFromSession(session);

		postService.removePost(postId, user.getId());
	}

	private static UserSearchDto getUserFromSession(HttpSession session) {
		return Optional.ofNullable((UserSearchDto)session.getAttribute(session.getId()))
			.orElseThrow(() -> new IllegalStateException("로그인 되어있지 않습니다."));
	}
}
