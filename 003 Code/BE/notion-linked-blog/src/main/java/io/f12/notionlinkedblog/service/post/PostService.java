package io.f12.notionlinkedblog.service.post;

import static io.f12.notionlinkedblog.error.Error.PostExceptions.*;
import static io.f12.notionlinkedblog.error.Error.UserExceptions.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.f12.notionlinkedblog.domain.post.Post;
import io.f12.notionlinkedblog.domain.post.dto.PostSearchDto;
import io.f12.notionlinkedblog.domain.user.User;
import io.f12.notionlinkedblog.repository.post.PostRepository;
import io.f12.notionlinkedblog.repository.user.UserDataRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

	private final PostRepository postRepository;
	private final UserDataRepository userDataRepository;

	public Post createPost(Long userId, String title, String content, String thumbnail) {

		User findUser = userDataRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));

		Post post = Post.builder()
			.user(findUser)
			.title(title)
			.content(content)
			.thumbnail(thumbnail)
			.viewCount(0L)
			.build();

		return postRepository.save(post);
	}

	public List<PostSearchDto> getPostsByTitle(String searchParam) {
		return postRepository.findPostByTitle(searchParam);
	}

	public List<PostSearchDto> getPostByContent(String searchParam) {
		return postRepository.findPostByContent(searchParam);
	}

	public PostSearchDto getPostDtoById(Long id) {
		return postRepository.findPostDtoById(id)
			.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST));
	}

	public Post getRawPostById(Long id) {
		return postRepository.findPostById(id)
			.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST));
	}

	public void removePost(Long postId, Long userId) {
		postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST));
		postRepository.removePostByIdAndUserId(postId, userId);
	}

	public void editPost(Long postId, Long userId, String title, String content, String thumbnail) {
		Post changedPost = postRepository.findPostById(postId)
			.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST));
		if (!changedPost.sameUser(userId)) {
			throw new IllegalStateException(WRITER_USER_NOT_MATCH);
		}
		changedPost.editPost(title, content, thumbnail);
	}
	//TODO: editSeries, 시리즈만 편집기능 필요

}
