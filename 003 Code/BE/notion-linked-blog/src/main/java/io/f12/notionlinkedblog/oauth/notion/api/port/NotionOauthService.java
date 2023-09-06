package io.f12.notionlinkedblog.oauth.notion.api.port;

import io.f12.notionlinkedblog.common.exceptions.exception.TokenAvailabilityFailureException;
import io.f12.notionlinkedblog.oauth.notion.api.response.NotionOAuthLinkDto;

public interface NotionOauthService {

	public NotionOAuthLinkDto getNotionAuthSite();

	public String saveAccessToken(String code, Long userId) throws TokenAvailabilityFailureException;

	public void removeAccessToken(Long userId);
}
