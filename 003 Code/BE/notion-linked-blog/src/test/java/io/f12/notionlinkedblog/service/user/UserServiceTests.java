package io.f12.notionlinkedblog.service.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import io.f12.notionlinkedblog.domain.dummy.DummyObject;
import io.f12.notionlinkedblog.domain.user.User;
import io.f12.notionlinkedblog.domain.user.dto.info.UserSearchDto;
import io.f12.notionlinkedblog.domain.user.dto.signup.UserSignupRequestDto;
import io.f12.notionlinkedblog.repository.user.UserDataRepository;
import io.f12.notionlinkedblog.repository.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTests extends DummyObject {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserDataRepository userDataRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@DisplayName("유저 이메일 기반 회원가입")
	@Nested
	class UserSignupByEmailTests {
		@DisplayName("정상 케이스")
		@Nested
		class SuccessCase {
			@DisplayName("이메일로 회원가입 성공")
			@Test
			void findByEmail() {
				//given
				UserSignupRequestDto newUserDto = UserSignupRequestDto.builder()
					.email("test@gmail.com")
					.username("test")
					.password("1234")
					.build();

				User mockUser = newMockUser(1L, "test", "test@gmail.com");

				// stub 1
				given(userRepository.findByEmail(any())).willReturn(Optional.empty());

				// stub 2
				given(userRepository.save(any())).willReturn(mockUser);

				//when
				Long id = userService.signupByEmail(newUserDto);

				//then
				assertThat(id).isEqualTo(mockUser.getId());
			}
		}

		@DisplayName("비정상 케이스")
		@Nested
		class FailureCase {
			@DisplayName("이메일 중복으로 인한 회원가입 실패")
			@Test
			void findByEmail() {
				//given
				UserSignupRequestDto existUserDto = UserSignupRequestDto.builder()
					.email("test@gmail.com")
					.username("test")
					.password("password")
					.build();
				User existUser = existUserDto.toEntity();
				userRepository.save(existUser);

				UserSignupRequestDto newUserDto = UserSignupRequestDto.builder()
					.email("test@gmail.com")
					.username("test")
					.password("password")
					.build();
				User newUser = newUserDto.toEntity();
				given(userRepository.findByEmail(newUser.getEmail())).willReturn(Optional.of(existUser));

				//then
				assertThatThrownBy(() -> userService.signupByEmail(newUserDto)).isInstanceOf(
					IllegalArgumentException.class);
			}
		}
	}

	@DisplayName("유저 변경, 조회 및 삭제")
	@Nested
	class UserInfoTest {

		@DisplayName("유저 조회 테스트")
		@Nested
		class UserCheckTest {
			@Test
			@DisplayName("정보 가져오기")
			void getUserInfoTest() {
				User userA = User.builder()
					.email("test1@gmail.com")
					.username("username1")
					.password("password1")
					.build();
				User userB = User.builder()
					.email("test2@gmail.com")
					.username("username2")
					.password("password2")
					.build();
				Long fakeIdForA = 1L;
				Long fakeIdForB = 2L;

				ReflectionTestUtils.setField(userA, "id", fakeIdForA);
				ReflectionTestUtils.setField(userB, "id", fakeIdForB);
				//Mock
				UserSearchDto mockUserSearchDtoA = UserSearchDto.builder()
					.id(fakeIdForA)
					.email("test1@gmail.com")
					.username("username1")
					.build();
				UserSearchDto mockUserSearchDtoB = UserSearchDto.builder()
					.id(fakeIdForB)
					.email("test2@gmail.com")
					.username("username2")
					.build();
				given(userDataRepository.findUserByDto(fakeIdForA))
					.willReturn(Optional.ofNullable(mockUserSearchDtoA));
				given(userDataRepository.findUserByDto(fakeIdForB))
					.willReturn(Optional.ofNullable(mockUserSearchDtoB));
			}

		}

		@DisplayName("유저 변경 테스트")
		@Nested
		class UserEditTest {
			@Test
			@DisplayName("유저 정보 변경")
			void editUserInfoTest() {
				User userA = User.builder()
					.email("test1@gmail.com")
					.username("username1")
					.password("password1")
					.build();
				User editedUser = User.builder()
					.email("changed@gmail.com")
					.username("changedUsername")
					.password("changedPassword")
					.build();
				Long fakeIdForA = 1L;

				ReflectionTestUtils.setField(userA, "id", fakeIdForA);
				ReflectionTestUtils.setField(editedUser, "id", fakeIdForA);
				given(userDataRepository.findById(fakeIdForA))
					.willReturn(Optional.of(userA));

				String returnValue = userService.editUserInfo(editedUser.getId(), editedUser.getUsername(),
					editedUser.getEmail(), editedUser.getPassword(), editedUser.getProfile(), editedUser.getBlogTitle(),
					editedUser.getGithubLink(), editedUser.getInstagramLink(), editedUser.getIntroduction());
				assertThat(returnValue).isSameAs("success");
			}

			@Test
			@DisplayName("유저 정보 변경 실패 - 해당 유저가 존재하지 않을때")
			void editUnUnifiedUserInfoTest() {
				User editedUser = User.builder()
					.email("changed@gmail.com")
					.username("changedUsername")
					.password("changedPassword")
					.build();
				Long fakeIdForA = 1L;
				ReflectionTestUtils.setField(editedUser, "id", fakeIdForA);

				String returnValue = userService.editUserInfo(editedUser.getId(), editedUser.getUsername(),
					editedUser.getEmail(), editedUser.getPassword(), editedUser.getProfile(), editedUser.getBlogTitle(),
					editedUser.getGithubLink(), editedUser.getInstagramLink(), editedUser.getIntroduction());
				assertThat(returnValue).isEqualTo("error");
			}
		}

	}
}
