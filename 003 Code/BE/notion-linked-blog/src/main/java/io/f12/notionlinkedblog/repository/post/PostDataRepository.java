package io.f12.notionlinkedblog.repository.post;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.f12.notionlinkedblog.domain.post.Post;

@Repository
public interface PostDataRepository extends JpaRepository<Post, Long> {
	@Query("SELECT p "
		+ "FROM Post p join fetch p.user left join fetch p.likes "
		+ "WHERE p.title LIKE %:name%")
		//TODO: HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
	Slice<Post> findByTitle(@Param("name") String name, Pageable pageable);

	@Query("SELECT p "
		+ "FROM Post p join fetch p.user u left join fetch p.likes "
		+ "WHERE p.content LIKE %:content%")
		//TODO: HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
	Slice<Post> findByContent(@Param("content") String content, Pageable pageable);

	@Query("SELECT DISTINCT p "
		+ "FROM Post p join fetch p.user left join fetch p.likes "
		+ "WHERE p.id = :id")
	Optional<Post> findById(@Param("id") Long id);

	@Query("SELECT p "
		+ "FROM Post p join fetch p.user u left join fetch p.likes "
		+ "ORDER BY p.createdAt DESC")
		//TODO: HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
	Slice<Post> findLatestByCreatedAtDesc(Pageable pageable);

	@Query("SELECT p "
		+ "FROM Post p join fetch p.user u left join fetch p.likes "
		+ "ORDER BY p.popularity DESC")
		//TODO: HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
	Slice<Post> findPopularityByViewCountAtDesc(Pageable pageable);
}
