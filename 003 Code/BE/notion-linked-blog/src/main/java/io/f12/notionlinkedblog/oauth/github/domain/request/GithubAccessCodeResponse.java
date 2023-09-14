package io.f12.notionlinkedblog.oauth.github.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GithubAccessCodeResponse {
	@JsonProperty("access_token")
	String accessToken;
	String scope;
	@JsonProperty("token_type")
	String tokenType;
}
