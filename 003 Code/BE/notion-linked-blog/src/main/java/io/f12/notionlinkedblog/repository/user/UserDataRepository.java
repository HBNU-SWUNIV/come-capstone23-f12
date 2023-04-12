package io.f12.notionlinkedblog.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.f12.notionlinkedblog.domain.user.User;
import io.f12.notionlinkedblog.domain.user.dto.info.UserSearchDto;

public interface UserDataRepository extends JpaRepository<User, Long> {

	@Query(
		"select new io.f12.notionlinkedblog.domain.user.dto.info.UserSearchDto(u.id,"
			+ "u.username,u.email,u.profile,u.introduction,u.blogTitle,u.githubLink,u.instagramLink) "
			+ "from User u "
			+ "where u.id = :id")
	Optional<UserSearchDto> findUserById(@Param("id") Long id);
}
