package io.f12.notionlinkedblog.comments.service.port;

import java.util.List;
import java.util.Optional;

import io.f12.notionlinkedblog.domain.comments.Comments;

public interface CommentsRepository {
	List<Comments> findByPostId(Long postId);

	Optional<Comments> findById(Long commentsId);

	Comments save(Comments comments);

	void deleteById(Long commentId);

	void deleteAll();
}
