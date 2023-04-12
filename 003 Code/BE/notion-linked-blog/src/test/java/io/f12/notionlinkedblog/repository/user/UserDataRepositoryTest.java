package io.f12.notionlinkedblog.repository.user;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.f12.notionlinkedblog.domain.user.User;
import io.f12.notionlinkedblog.domain.user.dto.info.UserSearchDto;

@DataJpaTest
class UserDataRepositoryTest {

	@Autowired
	private UserDataRepository userDataRepository;

	@DisplayName("유저 조회 테스트")
	@Nested
	@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
	class UserSelectingTests {

		@Test
		@DisplayName("정상 조회")
		@Order(1)
		void specificUserDTOCheck() {
			User user1 = User.builder()
				.username("username1")
				.email("email1")
				.password("password1")
				.profile("profile1")
				.introduction("intro1")
				.blogTitle("title1")
				.githubLink("git1")
				.instagramLink("insta1")
				.build();
			User user2 = User.builder()
				.username("username2")
				.email("email2")
				.password("password2")
				.profile("profile2")
				.introduction("intro2")
				.blogTitle("title2")
				.githubLink("git2")
				.instagramLink("insta2")
				.build();
			userDataRepository.save(user1);
			userDataRepository.save(user2);

			long id1 = 1L;
			Optional<UserSearchDto> userA = userDataRepository.findUserById(id1);
			UserSearchDto findUserA = userA.orElseThrow(
				() -> new IllegalArgumentException("Wrong MemberId: " + id1));
			long id2 = 2L;
			Optional<UserSearchDto> userB = userDataRepository.findUserById(id2);
			UserSearchDto findUserB = userB.orElseThrow(
				() -> new IllegalArgumentException("Wrong MemberId: " + id2));

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
		@Order(2)
		@Test
		void unUnifiedUserDtoCheck() {
			User user1 = User.builder()
				.username("username1")
				.email("email1")
				.password("password1")
				.profile("profile1")
				.introduction("intro1")
				.blogTitle("title1")
				.githubLink("git1")
				.instagramLink("insta1")
				.build();
			userDataRepository.save(user1);

			long id = 1L;
			String errorMessage = "Wrong MemberId: ";
			Optional<UserSearchDto> userA = userDataRepository.findUserById(id);
			assertThatThrownBy(() -> {
				userA.orElseThrow(() -> new IllegalArgumentException(errorMessage + id));
			}).isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining(errorMessage)
				.hasMessageContaining(String.valueOf(id));

		}

	}
}
