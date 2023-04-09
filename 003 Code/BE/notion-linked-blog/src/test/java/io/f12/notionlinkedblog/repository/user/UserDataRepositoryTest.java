package io.f12.notionlinkedblog.repository.user;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.f12.notionlinkedblog.domain.user.User;
import io.f12.notionlinkedblog.domain.user.dto.info.UserSearchDto;

@DataJpaTest
class UserDataRepositoryTest {

	@Autowired
	private UserDataRepository userDataRepository;

	@DisplayName("유조 조회 리포지토리")
	@Nested
	class UserSearchTest {
		@DisplayName("정상조회")
		@Test
		void specificUserDTOCheck() {
			User user1 = new User(1L, "username1", "email1", "password1", "profile1", "intro1", "blogTitle1", "git1",
				"insta1");
			User user2 = new User(2L, "username2", "email2", "password2", "profile2", "intro2", "blogTitle2", "git2",
				"insta2");
			userDataRepository.save(user1);
			userDataRepository.save(user2);

			long id1 = 1L;
			Optional<UserSearchDto> userA = userDataRepository.findUserByDto(id1);
			UserSearchDto findUserA = userA.orElseThrow(() -> new IllegalArgumentException("Wrong MemberId: " + id1));
			long id2 = 2L;
			Optional<UserSearchDto> userB = userDataRepository.findUserByDto(id2);
			UserSearchDto findUserB = userB.orElseThrow(() -> new IllegalArgumentException("Wrong MemberId: " + id2));

			assertThat(findUserA).extracting("username").isEqualTo(user1.getUsername());
			assertThat(findUserA).extracting("email").isEqualTo(user1.getEmail());
			assertThat(findUserA).extracting("profile").isEqualTo(user1.getProfile());
			assertThat(findUserA).extracting("introduction").isEqualTo(user1.getIntroduction());
			assertThat(findUserA).extracting("blogTitle").isEqualTo(user1.getBlogTitle());
			assertThat(findUserA).extracting("githubLink").isEqualTo(user1.getGithubLink());
			assertThat(findUserA).extracting("instagramLink").isEqualTo(user1.getInstagramLink());

			assertThat(findUserB).extracting("username").isEqualTo(user2.getUsername());
			assertThat(findUserB).extracting("email").isEqualTo(user2.getEmail());
			assertThat(findUserB).extracting("profile").isEqualTo(user2.getProfile());
			assertThat(findUserB).extracting("introduction").isEqualTo(user2.getIntroduction());
			assertThat(findUserB).extracting("blogTitle").isEqualTo(user2.getBlogTitle());
			assertThat(findUserB).extracting("githubLink").isEqualTo(user2.getGithubLink());
			assertThat(findUserB).extracting("instagramLink").isEqualTo(user2.getInstagramLink());

		}

		@DisplayName("비정상 조회 - 없는 회원 조회시")
		@Test
		void unUnifiedUserDtoCheck() {
			User user1 = new User(1L, "username1", "email1", "password1", "profile1", "intro1", "blogTitle1", "git1",
				"insta1");
			userDataRepository.save(user1);

			long id = 2L;
			String errorMessage = "Wrong MemberId: ";
			Optional<UserSearchDto> userA = userDataRepository.findUserByDto(id);
			assertThatThrownBy(() -> {
				userA.orElseThrow(() -> new IllegalArgumentException(errorMessage + id));
			}).isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining(errorMessage)
				.hasMessageContaining(String.valueOf(id));

		}

	}

}