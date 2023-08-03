package io.f12.notionlinkedblog.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.f12.notionlinkedblog.domain.user.User;

public interface UserDataRepository extends JpaRepository<User, Long> {
	@Query("SELECT u FROM User u left join fetch u.notionOauth WHERE u.id = :id")
	Optional<User> findUserById(@Param("id") Long id);

	Optional<User> findByEmail(final String email);

	@Query("SELECT u "
		+ "FROM User u "
		+ "LEFT JOIN FETCH u.series "
		+ "WHERE u.id = :userId")
	Optional<User> findSeriesByUserId(@Param("userId") Long userId);
}
