package io.f12.notionlinkedblog.oauth.github.service.port;

import io.f12.notionlinkedblog.oauth.common.infrastructure.CommonOAuthEntity;

public interface GithubOauthDataRepository {
	CommonOAuthEntity save(CommonOAuthEntity entity);
}
