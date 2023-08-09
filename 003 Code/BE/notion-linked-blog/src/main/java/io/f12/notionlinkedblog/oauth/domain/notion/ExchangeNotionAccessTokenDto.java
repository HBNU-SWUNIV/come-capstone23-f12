package io.f12.notionlinkedblog.oauth.domain.notion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ExchangeNotionAccessTokenDto {
	private String grantType;
	private String code;
	private String redirectUri;
}
