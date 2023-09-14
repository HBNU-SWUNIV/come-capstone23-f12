package io.f12.notionlinkedblog.oauth.common.exception;

public class TokenIssuanceFailureException extends Exception {
	public TokenIssuanceFailureException() {
		super("엑세스 토큰 발급에 실패하였습니다.");
	}

	public TokenIssuanceFailureException(String message) {
		super(message);
	}
}
