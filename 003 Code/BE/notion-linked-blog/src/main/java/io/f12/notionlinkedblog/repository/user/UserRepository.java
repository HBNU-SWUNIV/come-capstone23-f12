package io.f12.notionlinkedblog.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.f12.notionlinkedblog.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(final String email);
}
