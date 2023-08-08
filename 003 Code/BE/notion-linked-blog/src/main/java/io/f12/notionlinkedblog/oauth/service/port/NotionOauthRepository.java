package io.f12.notionlinkedblog.oauth.service.port;

import java.util.Optional;

import io.f12.notionlinkedblog.domain.oauth.NotionOauth;

public interface NotionOauthRepository {
	Optional<NotionOauth> findNotionOauthByUserId(Long userId);

	void deleteNotionOauthByUserId(Long userId);

	NotionOauth save(NotionOauth notionOauth);
}
