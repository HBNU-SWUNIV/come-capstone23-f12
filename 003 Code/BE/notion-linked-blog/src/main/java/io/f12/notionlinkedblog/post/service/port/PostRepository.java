package io.f12.notionlinkedblog.post.service.port;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

import io.f12.notionlinkedblog.domain.post.Post;

public interface PostRepository {
	Optional<Post> findById(@Param("id") Long id);

	List<Post> findByPostIdForTrend();

	String findThumbnailPathWithName(@Param("thumbnailName") String name);

	Post save(Post post);

	void deleteById(Long id);

	void deleteAll();
}
