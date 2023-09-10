package io.f12.notionlinkedblog.oauth.github.domain.info;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Service
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GithubOAuthComponent {
	@Value("${external.github.authorizeUrl}")
	private String authUrl;
	@Value("${external.github.clientId}")
	private String clientId;
	@Value("${external.github.clientSecret}")
	private String clientSecret;
}
