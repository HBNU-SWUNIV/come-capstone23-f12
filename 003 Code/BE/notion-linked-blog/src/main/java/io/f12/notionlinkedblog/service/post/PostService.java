package io.f12.notionlinkedblog.service.post;

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
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

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
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포스트입니다."));
	}

	public Post getRawPostById(Long id) {
		return postRepository.findPostById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포스트입니다."));
	}

	public void removePost(Long postId, Long userId) {
		postRepository.removePostByIdAndUserId(postId, userId);
	}

	public void editPost(Long postId, Long userId, String title, String content, String thumbnail) {
		Post changedPost = postRepository.findPostById(postId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포스트입니다."));
		if (!changedPost.sameUser(userId)) {
			throw new IllegalStateException("글 작성자와 일치하지 않는 사용자입니다.");
		}
		changedPost.editPost(title, content, thumbnail);
	}
	//TODO: editSeries, 시리즈만 편집기능 필요

}
