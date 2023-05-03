package io.f12.notionlinkedblog.api.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.f12.notionlinkedblog.api.common.Endpoint;
import io.f12.notionlinkedblog.domain.post.Post;
import io.f12.notionlinkedblog.domain.post.dto.PostCreateDto;
import io.f12.notionlinkedblog.domain.post.dto.PostEditDto;
import io.f12.notionlinkedblog.domain.user.User;
import io.f12.notionlinkedblog.domain.user.dto.info.UserSearchDto;
import io.f12.notionlinkedblog.repository.post.PostRepository;
import io.f12.notionlinkedblog.repository.user.UserDataRepository;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class PostApiControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserDataRepository userDataRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	private User testUser;
	private Post testPost;

	@BeforeEach
	void init() {
		testUser = userDataRepository.save(User.builder()
			.email("test@gmail.com")
			.username("test")
			.password(passwordEncoder.encode("1234"))
			.build()
		);
		testPost = postRepository.save(Post.builder()
			.user(testUser)
			.title("testTitle")
			.content("testContent").build());
	}

	@AfterEach
	void clear() {
		postRepository.deleteAll();
		userDataRepository.deleteAll();
	}

	@DisplayName("포스트 생성")
	@Nested
	class createPost {
		@DisplayName("성공 케이스")
		@WithUserDetails(value = "test@gmail.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
		@Test
		void successCase() throws Exception {
			//given
			final String url = Endpoint.Api.POST;
			UserSearchDto user = userDataRepository.findUserById(testUser.getId())
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

			PostCreateDto body = PostCreateDto.builder()
				.title("testTitle")
				.content("testContent")
				.thumbnail("testThumbnail")
				.build();
			String requestBody = objectMapper.writeValueAsString(body);
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
				final String url = Endpoint.Api.POST;
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
					String url = Endpoint.Api.POST + "/" + testPost.getId();
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
					String url = Endpoint.Api.POST + "/get/title";
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
						String url = Endpoint.Api.POST + "/get/title";
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
					String url = Endpoint.Api.POST + "/get/content";
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
						String url = Endpoint.Api.POST + "/get/content";
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
			String url = Endpoint.Api.POST + "/" + testPost.getId();

			UserSearchDto user = userDataRepository.findUserById(testUser.getId())
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID입니다"));

			PostEditDto body = PostEditDto.builder()
				.title("testTitle")
				.content("testContent")
				.thumbnail("testThumbnail")
				.build();

			String requestBody = objectMapper.writeValueAsString(body);
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
				String url = Endpoint.Api.POST + "/" + testPost.getId();

				PostEditDto body = PostEditDto.builder()
					.title("testTitle")
					.content("testContent")
					.thumbnail("testThumbnail")
					.build();

				String requestBody = objectMapper.writeValueAsString(body);
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
			String url = Endpoint.Api.POST + "/" + testPost.getId();
			UserSearchDto user = userDataRepository.findUserById(testUser.getId())
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID입니다"));
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
				String url = Endpoint.Api.POST + "/" + testUser.getId();
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