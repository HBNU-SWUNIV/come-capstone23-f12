package io.f12.notionlinkedblog.user.service.port;

import java.util.Optional;

import io.f12.notionlinkedblog.domain.user.User;

public interface UserRepository {
	Optional<User> findUserById(Long id);

	Optional<User> findUserByIdForNotionAuthToken(Long id);

	Optional<User> findByEmail(final String email);

	Optional<User> findSeriesByUserId(Long userId);

	User save(User user);

	Optional<User> findById(Long id);

	void deleteById(Long id);

	void deleteAll();

}
