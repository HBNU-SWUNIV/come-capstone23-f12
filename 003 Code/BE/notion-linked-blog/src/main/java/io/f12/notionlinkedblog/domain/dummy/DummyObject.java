package io.f12.notionlinkedblog.domain.dummy;

import io.f12.notionlinkedblog.domain.user.UserEntity;
import io.f12.notionlinkedblog.domain.verification.EmailVerificationToken;

public class DummyObject {

	protected EmailVerificationToken newMockEmailVerificationToken(String id, String code) {
		return EmailVerificationToken.builder()
			.id(id)
			.code(code)
			.email("test@gmail.com")
			.build();
	}

	protected UserEntity newMockUser(Long id, String username, String email) {
		return UserEntity.builder()
			.id(id)
			.username(username)
			.email(email)
			.password("1234")
			.profile("hello")
			.introduction("Hello!")
			.blogTitle("BT")
			.githubLink("GITHUB")
			.instagramLink("INSTAGRAM")
			.build();
	}
}
