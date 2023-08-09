package io.f12.notionlinkedblog.oauth.domain.notion;

import io.f12.notionlinkedblog.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class NotionOauth {
	private Long id;
	private User user;
	private String accessToken;
	private String botId;
}
