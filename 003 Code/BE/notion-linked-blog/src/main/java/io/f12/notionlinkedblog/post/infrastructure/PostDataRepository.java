package io.f12.notionlinkedblog.post.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.f12.notionlinkedblog.domain.post.Post;
import io.f12.notionlinkedblog.post.service.port.PostRepository;

@Repository
public interface PostDataRepository extends JpaRepository<Post, Long>, PostRepository {

	@Override
	@Query("SELECT p "
		+ "FROM Post p join fetch p.user left join fetch p.likes "
		+ "WHERE p.id = :id")
	Optional<Post> findById(@Param("id") Long id);

	@Override
	@Query("SELECT DISTINCT p FROM Post p left join fetch p.likes")
	List<Post> findByPostIdForTrend();

	@Override
	@Query("SELECT DISTINCT p.storedThumbnailPath FROM Post p WHERE p.thumbnailName  = :thumbnailName")
	String findThumbnailPathWithName(@Param("thumbnailName") String name);
}
