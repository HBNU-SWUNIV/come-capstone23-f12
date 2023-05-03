package io.f12.notionlinkedblog.error;

public class Error {
	public static class UserExceptions {
		public static final String USER_NOT_EXIST = "존재하지 않는 유저입니다.";
		public static final String EMAIL_ALREADY_EXIST = "이미 존재하는 이메일입니다.";
	}

	public static class PostExceptions {
		public static final String POST_NOT_EXIST = "존재하지 않는 포스트입니다.";
		public static final String WRITER_USER_NOT_MATCH = "글 작성자와 사용자가 일치하지 않습니다.";
	}
}
