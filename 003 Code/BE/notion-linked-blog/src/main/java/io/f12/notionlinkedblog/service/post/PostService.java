package io.f12.notionlinkedblog.service.post;

import static io.f12.notionlinkedblog.exceptions.Exceptions.PostExceptions.*;
import static io.f12.notionlinkedblog.exceptions.Exceptions.UserExceptions.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.f12.notionlinkedblog.domain.post.Post;
import io.f12.notionlinkedblog.domain.post.dto.PostCreateDto;
import io.f12.notionlinkedblog.domain.post.dto.PostEditDto;
import io.f12.notionlinkedblog.domain.post.dto.PostSearchDto;
import io.f12.notionlinkedblog.domain.user.User;
import io.f12.notionlinkedblog.repository.post.PostDataRepository;
import io.f12.notionlinkedblog.repository.user.UserDataRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

	private final PostDataRepository postDataRepository;
	private final UserDataRepository userDataRepository;

	public PostSearchDto createPost(Long userId, PostCreateDto postCreateDto) {

		User findUser = userDataRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));

		Post post = Post.builder()
			.user(findUser)
			.title(postCreateDto.getTitle())
			.content(postCreateDto.getContent())
			.thumbnail(postCreateDto.getContent())
			.viewCount(0L)
			.build();

		Post savedPost = postDataRepository.save(post);

		return PostSearchDto.builder()
			.username(savedPost.getUser().getUsername())
			.title(savedPost.getTitle())
			.content(savedPost.getContent())
			.thumbnail(savedPost.getThumbnail())
			.viewCount(savedPost.getViewCount())
			.build();
	}

	public List<PostSearchDto> getPostsByTitle(String searchParam) {
		return postDataRepository.findByTitle(searchParam);
	}

	public List<PostSearchDto> getPostByContent(String searchParam) {
		return postDataRepository.findByContent(searchParam);
	}

	public PostSearchDto getPostDtoById(Long id) {
		return postDataRepository.findDtoById(id)
			.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST));
	}

	public Post getPostById(Long id) {
		return postDataRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST));
	}

	public void removePost(Long postId, Long userId) {
		postDataRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST));
		postDataRepository.removeByIdAndUserId(postId, userId);
	}

	public void editPost(Long postId, Long userId, PostEditDto postEditDto) {
		Post changedPost = postDataRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST));
		if (!changedPost.isSameUser(userId)) {
			throw new IllegalStateException(WRITER_USER_NOT_MATCH);
		}
		changedPost.editPost(postEditDto.getTitle(), postEditDto.getContent(), postEditDto.getThumbnail());
	}
	//TODO: editSeries, 시리즈만 편집기능 필요

}
