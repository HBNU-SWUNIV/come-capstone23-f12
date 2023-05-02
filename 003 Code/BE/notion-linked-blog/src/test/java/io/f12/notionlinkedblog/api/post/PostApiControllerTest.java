package io.f12.notionlinkedblog.api.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

import io.f12.notionlinkedblog.domain.post.dto.PostCreateDto;
import io.f12.notionlinkedblog.domain.post.dto.PostEditDto;
import io.f12.notionlinkedblog.domain.user.dto.info.UserSearchDto;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class PostApiControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@DisplayName("포스트 생성")
	@Nested
	class createPost {
		@DisplayName("성공 케이스")
		@Test
		void successCase() throws Exception {
			//given
			final String url = "/api/post";
			UserSearchDto user = UserSearchDto.builder()
				.username("tester")
				.email("test@test.com")
				.build();

			PostCreateDto body = PostCreateDto.builder()
				.title("testTitle")
				.content("testContent")
				.thumbnail("testThumbnail")
				.build();
			String requestBody = objectMapper.writeValueAsString(body);
			ReflectionTestUtils.setField(user, "id", 1L);
			//mock
			MockHttpSession mockHttpSession = new MockHttpSession();
			mockHttpSession.setAttribute(mockHttpSession.getId(), user);

			//when
			ResultActions resultActions = mockMvc.perform(
				put(url)
					.contentType(MediaType.APPLICATION_JSON)
					.session(mockHttpSession)
					.content(requestBody)
			);
			//then
			resultActions.andExpect(status().isCreated());
		}

		@DisplayName("실패 케이스")
		@Nested
		class failureCase {

			@DisplayName("미 로그인")
			@Test
			void unLogin() throws Exception {
				//given
				final String url = "/api/post";
				PostCreateDto body = PostCreateDto.builder()
					.title("testTitle")
					.content("testContent")
					.thumbnail("testThumbnail")
					.build();
				String requestBody = objectMapper.writeValueAsString(body);

				//mock
				MockHttpSession mockHttpSession = new MockHttpSession();

				//when
				ResultActions resultActions = mockMvc.perform(
					put(url)
						.contentType(MediaType.APPLICATION_JSON)
						.cookie(new Cookie("JSESSIONID", mockHttpSession.getId()))
						.session(mockHttpSession)
						.content(requestBody)
				);
				//then
				resultActions.andExpect(status().isBadRequest());
			}

		}
	}

	@DisplayName("포스트 조회")
	@Nested
	class getPost {

		@DisplayName("단건 조회")
		@Nested
		class singleLookup {
			@DisplayName("포스트 ID로 조회")
			@Nested
			class getPostById {
				@DisplayName("성공 케이스")
				@Test
				void successCase() throws Exception {
					//given
					String url = "/api/post/1";
					//when
					ResultActions resultActions = mockMvc.perform(
						get(url)
					);
					//then
					resultActions.andExpect(status().isOk());

				}
			}
		}

		@DisplayName("다건 조회")
		@Nested
		class multiLookup {
			@DisplayName("포스트 title 로 조회")
			@Nested
			class getPostsByTitle {
				@DisplayName("성공 케이스")
				@Test
				void successCase() throws Exception {
					//given
					String url = "/api/post/get/title";
					//when
					ResultActions resultActions = mockMvc.perform(
						get(url)
							.param("title", "test")
					);
					//then
					resultActions.andExpect(status().isOk());
				}

				@DisplayName("실패 케이스")
				@Nested
				class failureCase {
					@DisplayName("파라미터 미존재")
					@Test
					void noParam() throws Exception {
						//given
						String url = "/api/post/get/title";
						//when
						ResultActions resultActions = mockMvc.perform(
							get(url)
						);
						//then
						resultActions.andExpect(status().isBadRequest());
					}
				}
			}

			@DisplayName("포스트 content 로 조회")
			@Nested
			class getPostsByContent {
				@DisplayName("성공 케이스")
				@Test
				void successCase() throws Exception {
					//given
					String url = "/api/post/get/content";
					//when
					ResultActions resultActions = mockMvc.perform(
						get(url)
							.param("content", "content")
					);
					//then
					resultActions.andExpect(status().isOk());
				}

				@DisplayName("실패 케이스")
				@Nested
				class failureCase {
					@DisplayName("파라미터 미존재")
					@Test
					void noParam() throws Exception {
						//given
						String url = "/api/post/get/content";
						//when
						ResultActions resultActions = mockMvc.perform(
							get(url)
						);
						//then
						resultActions.andExpect(status().isBadRequest());
					}

				}
			}
		}

	}

	@DisplayName("포스트 수정")
	@Nested
	class editPost {
		@DisplayName("성공 케이스")
		@Test
		void successCase() throws Exception {
			//given
			String url = "/api/post/1";

			UserSearchDto user = UserSearchDto.builder()
				.username("tester")
				.email("test@test.com")
				.build();

			PostEditDto body = PostEditDto.builder()
				.title("testTitle")
				.content("testContent")
				.thumbnail("testThumbnail")
				.build();

			String requestBody = objectMapper.writeValueAsString(body);
			ReflectionTestUtils.setField(user, "id", 1L);
			//mock
			MockHttpSession mockHttpSession = new MockHttpSession();
			mockHttpSession.setAttribute(mockHttpSession.getId(), user);
			//when
			ResultActions resultActions = mockMvc.perform(
				put(url)
					.session(mockHttpSession)
					.content(requestBody)
					.contentType(MediaType.APPLICATION_JSON)
			);
			//then
			resultActions.andExpectAll(
				status().isFound(),
				redirectedUrl(url)
			);
		}

		@DisplayName("실패 케이스")
		@Nested
		class failureCase {
			@DisplayName("세션 미존재")
			@Test
			void sessionUnavailable() throws Exception {
				//given
				String url = "/api/post/1";

				UserSearchDto user = UserSearchDto.builder()
					.username("tester")
					.email("test@test.com")
					.build();

				PostEditDto body = PostEditDto.builder()
					.title("testTitle")
					.content("testContent")
					.thumbnail("testThumbnail")
					.build();

				String requestBody = objectMapper.writeValueAsString(body);
				ReflectionTestUtils.setField(user, "id", 1L);
				//when
				ResultActions resultActions = mockMvc.perform(
					put(url)
						.content(requestBody)
						.contentType(MediaType.APPLICATION_JSON)
				);
				//then
				resultActions.andExpect(status().isBadRequest());
			}
		}
	}

	@DisplayName("포스트 삭제")
	@Nested
	class removePost {
		@DisplayName("성공 케이스")
		@Test
		void successCase() throws Exception {
			//given
			String url = "/api/post/1";
			UserSearchDto user = UserSearchDto.builder()
				.username("tester")
				.email("test@test.com")
				.build();
			ReflectionTestUtils.setField(user, "id", 1L);
			//mock
			MockHttpSession mockHttpSession = new MockHttpSession();
			mockHttpSession.setAttribute(mockHttpSession.getId(), user);
			//when
			ResultActions resultActions = mockMvc.perform(
				delete(url)
					.session(mockHttpSession)
			);
			//then
			resultActions.andExpect(status().isNoContent());
		}

		@DisplayName("실패 케이스")
		@Nested
		class failureCase {
			@DisplayName("세션 미존재")
			@Test
			void sessionUnavailable() throws Exception {
				//given
				String url = "/api/post/1";
				//mock

				//when
				ResultActions resultActions = mockMvc.perform(
					delete(url)
				);
				//then
				resultActions.andExpect(status().isBadRequest());
			}
		}
	}

}