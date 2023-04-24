package io.f12.notionlinkedblog.repository.post;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.f12.notionlinkedblog.domain.post.Post;
import io.f12.notionlinkedblog.domain.post.dto.PostSearchDto;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	@Query("SELECT NEW io.f12.notionlinkedblog.domain.post.dto.PostSearchDto"
		+ "(u.username, p.seriesId, p.title, p.content, p.thumbnail, p.viewCount)"
		+ "FROM Post p join p.user u "
		+ "WHERE p.id = :id")
	Optional<PostSearchDto> findPostDtoById(@Param("id") Long id);

	Optional<Post> findPostById(@Param("id") Long id);

	@Query("SELECT NEW io.f12.notionlinkedblog.domain.post.dto.PostSearchDto"
		+ "(u.username, p.seriesId, p.title, p.content, p.thumbnail, p.viewCount)"
		+ "FROM Post p join p.user u "
		+ "WHERE p.title LIKE %:name%")
	List<PostSearchDto> findPostByName(@Param("name") String name);

	@Query("DELETE FROM Post p WHERE p.id = :postId AND p.user.id =:userId")
	void removePostByIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);

}
