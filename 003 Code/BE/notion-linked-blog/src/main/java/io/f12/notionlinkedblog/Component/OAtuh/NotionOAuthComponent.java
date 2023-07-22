package io.f12.notionlinkedblog.Component.OAtuh;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Service
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotionOAuthComponent {
	@Value("${external.real.notionClientId}")
	private String clientId;
	@Value("${external.real.notionClientSecret}")
	private String clientSecret;
}
