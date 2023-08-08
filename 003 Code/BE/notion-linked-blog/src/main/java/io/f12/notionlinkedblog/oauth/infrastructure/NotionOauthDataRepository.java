package io.f12.notionlinkedblog.oauth.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.f12.notionlinkedblog.domain.oauth.NotionOauth;
import io.f12.notionlinkedblog.oauth.service.port.NotionOauthRepository;

public interface NotionOauthDataRepository extends JpaRepository<NotionOauth, Long>, NotionOauthRepository {
	Optional<NotionOauth> findNotionOauthByUserId(Long userId);

	void deleteNotionOauthByUserId(Long userId);
}
