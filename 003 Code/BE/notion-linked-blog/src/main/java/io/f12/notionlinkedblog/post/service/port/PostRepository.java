package io.f12.notionlinkedblog.post.service.port;

import java.util.List;
import java.util.Optional;

import io.f12.notionlinkedblog.domain.post.Post;

public interface PostRepository {
	Optional<Post> findById(Long id);

	List<Post> findByPostIdForTrend();

	String findThumbnailPathWithName(String name);

	Post save(Post post);

	void deleteById(Long id);

	void deleteAll();
}
