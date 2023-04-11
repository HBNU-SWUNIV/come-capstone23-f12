package io.f12.notionlinkedblog.api.user;

import static io.f12.notionlinkedblog.api.EmailApiController.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.f12.notionlinkedblog.domain.user.User;
import io.f12.notionlinkedblog.domain.user.dto.info.UserSearchDto;
import io.f12.notionlinkedblog.domain.user.dto.signup.UserSignupRequestDto;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class UserApiControllerTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper om;

	@DisplayName("이메일 기반 회원가입")
	@Nested
	class SignUpByEmailTests {
		@DisplayName("정상 케이스")
		@Nested
		class SuccessCase {
			@DisplayName("회원가입 성공")
			@Test
			void success() throws Exception {
				//given
				final String url = "/api/users/email/signup";
				UserSignupRequestDto userSignupRequestDto = UserSignupRequestDto.builder()
					.username("test")
					.email("test@gmail.com")
					.password("1234")
					.build();
				String requestBody = om.writeValueAsString(userSignupRequestDto);
				MockHttpSession mockHttpSession = new MockHttpSession();
				mockHttpSession.setAttribute(emailVerifiedAttr, "verified");

				//when
				ResultActions resultActions = mockMvc.perform(
					post(url)
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON)
						.session(mockHttpSession));

				//then
				resultActions.andExpect(status().isCreated());
			}
		}
	}

	@DisplayName("유저 정보 가져오기")
	@Test
	void getUserInfoTest() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/api/users/1"))
			.andDo(print());
		resultActions
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

	}

	@DisplayName("유저 정보 수정")
	@Test
	void editUserInfoTest() throws Exception {
		final String fakeSessionId = "fakeSessionId";
		final Long fakeUserId = 1L;
		final String url = "/api/users/1";
		User beforeUser = User.builder()
			.username("user1")
			.email("before@test.com")
			.password("1234")
			.build();
		UserSearchDto changedDto = UserSearchDto.builder()
			.username("changed")
			.email("changed@test.com")
			.build();

		ReflectionTestUtils.setField(beforeUser, "id", fakeUserId);
		ReflectionTestUtils.setField(changedDto, "id", fakeUserId);

		String requestBody = om.writeValueAsString(changedDto);
		MockHttpSession mockHttpSession = new MockHttpSession();
		mockHttpSession.setAttribute(fakeSessionId, beforeUser);
		ResultActions resultActions = mockMvc.perform(
				put(url)
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.cookie(new Cookie("JSESSIONID", fakeSessionId))
					.content(requestBody))
			.andDo(print());

		resultActions.andExpectAll(
			status().is3xxRedirection(),
			redirectedUrl(url)
		);

	}

	@DisplayName("유저 정보 삭제")
	@Test
	void removeUserInfoTest() throws Exception {
		final String fakeSessionId = "fakeSessionId";
		final Long fakeUserId = 1L;
		final String url = "/api/users/1";
		User removedUser = User.builder()
			.username("user1")
			.email("before@test.com")
			.password("1234")
			.build();

		ReflectionTestUtils.setField(removedUser, "id", fakeUserId);

		MockHttpSession mockHttpSession = new MockHttpSession();
		mockHttpSession.setAttribute(fakeSessionId, removedUser);
		ResultActions resultActions = mockMvc.perform(
				delete(url)
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.cookie(new Cookie("JSESSIONID", fakeSessionId))
			)
			.andDo(print());

		resultActions.andExpectAll(
			status().isOk(),
			content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN)
		);
	}
}
