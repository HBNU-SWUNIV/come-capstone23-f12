package io.f12.notionlinkedblog.oauth.notion.api.port;

import io.f12.notionlinkedblog.oauth.common.domain.response.OAuthLinkDto;
import io.f12.notionlinkedblog.oauth.common.exception.TokenAvailabilityFailureException;

public interface NotionOauthService {

	public OAuthLinkDto getNotionAuthSite();

	public String saveAccessToken(String code, Long userId) throws TokenAvailabilityFailureException;

	public void removeAccessToken(Long userId);
}
