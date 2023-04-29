package io.f12.notionlinkedblog.repository.post;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.f12.notionlinkedblog.domain.post.Post;
import io.f12.notionlinkedblog.domain.post.dto.PostSearchDto;
import io.f12.notionlinkedblog.domain.user.User;
import io.f12.notionlinkedblog.repository.user.UserDataRepository;

@DataJpaTest
class PostRepositoryTest {

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UserDataRepository userDataRepository;
	@Autowired
	private EntityManager entityManager;

	String title = "testTitle";
	String content = "testContent";

	@BeforeEach
	void init() {
		User user = User.builder()
			.username("tester")
			.email("test@test.com")
			.password("nope")
			.build();
		User savedUser = userDataRepository.save(user);

		Post post = Post.builder()
			.title(title)
			.content(content)
			.user(savedUser)
			.build();
		postRepository.save(post);
	}

	@AfterEach
	void clear() {
		postRepository.deleteAll();
		userDataRepository.deleteAll();
		entityManager.createNativeQuery("ALTER SEQUENCE user_seq RESTART WITH 1").executeUpdate();
		entityManager.createNativeQuery("ALTER SEQUENCE post_seq RESTART WITH 1").executeUpdate();
	}

	@DisplayName("포스트 조회")
	@Nested
	class findPost {

		@DisplayName("단건 조회")
		@Nested
		class singleSearch {

			@DisplayName("ID로 PostDto 조회")
			@Nested
			class findPostDtoById {

				@DisplayName("정상 조회")
				@Test
				void successCase() {
					//given
					//when
					userDataRepository.findById(1L)
						.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
					PostSearchDto searchPost = postRepository.findPostDtoById(1L)
						.orElseThrow(() -> new IllegalArgumentException("error"));

					//then
					assertThat(searchPost).extracting(PostSearchDto::getTitle).isEqualTo(title);
					assertThat(searchPost).extracting(PostSearchDto::getContent).isEqualTo(content);
				}

				@DisplayName("실패 케이스")
				@Nested
				class failureCase {
					@DisplayName("비정상 조회 - 없는 데이터 조회시")
					@Test
					void searchUnavailablePost() {
						//given
						String errorMessage = "WrongSearchId: ";
						String title = "testTitle";
						String content = "testContent";

						//when
						User savedUser = userDataRepository.findById(1L)
							.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

						Post post = Post.builder()
							.title(title)
							.content(content)
							.user(savedUser)
							.build();
						Post save = postRepository.save(post);
						long searchId = save.getId() + 1;

						//then
						Optional<PostSearchDto> postDtoById = postRepository.findPostDtoById(searchId);
						assertThatThrownBy(() -> {
							postDtoById.orElseThrow(() -> new IllegalArgumentException(errorMessage + searchId));
						}).isInstanceOf(IllegalArgumentException.class)
							.hasMessageContaining(errorMessage)
							.hasMessageContaining(String.valueOf(searchId));

					}
				}
			}

			@DisplayName("ID로 Post 조회")
			@Nested
			class findPostById {

				@DisplayName("정상 조회")
				@Test
				void successfulCase() {
					//given
					//when
					Post searchPostById = postRepository.findPostById(1L)
						.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포스트"));
					//then
					assertThat(searchPostById).extracting(Post::getTitle).isEqualTo(title);
					assertThat(searchPostById).extracting(Post::getContent).isEqualTo(content);

				}

				@DisplayName("실패 케이스")
				@Nested
				class failureCase {

					@DisplayName("존재하지 않는 포스트 조회")
					@Test
					void searchUnavailablePost() {
						//given
						String errorMessage = "WrongSearchId: ";
						Long postId = 2L;
						//when
						//then
						assertThatThrownBy(() -> {
							postRepository.findPostById(postId)
								.orElseThrow(() -> new IllegalArgumentException(errorMessage + postId));
						}).isInstanceOf(IllegalArgumentException.class)
							.hasMessageContaining(errorMessage)
							.hasMessageContaining(String.valueOf(postId));
					}

				}
			}

		}

		@DisplayName("다건 조회")
		@Nested
		class multiSearch {
			@DisplayName("제목으로 조회")
			@Nested
			class findPostsByTitle {

				@DisplayName("성공 케이스")
				@Nested
				class successfulCase {
					@DisplayName("정상 조회 - 데이터 0개")
					@Test
					void successfulCase_NoData() {
						//given
						String example = "NoData";
						//when
						List<PostSearchDto> postByTitle = postRepository.findPostByTitle(example);
						//then
						assertThat(postByTitle).isEmpty();
					}

					@DisplayName("정상 조회 - 데이터 1개")
					@Test
					void successfulCase_SingleData() {
						//given
						//when
						List<PostSearchDto> postByTitle = postRepository.findPostByTitle(title);
						PostSearchDto postSearchDto = postByTitle.get(0);
						//then
						assertThat(postByTitle).size().isEqualTo(1);
						assertThat(postSearchDto).extracting("title").isEqualTo(title);
						assertThat(postSearchDto).extracting("content").isEqualTo(content);

					}

					@DisplayName("정상 조회 - 데이터 2개 이상")
					@Test
					void successfulCase_MultiData() {
						//given
						User savedUser = userDataRepository.findById(1L)
							.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저"));

						Post post = Post.builder()
							.title(title + 2)
							.content(content + 2)
							.user(savedUser)
							.build();
						postRepository.save(post);
						//when
						List<PostSearchDto> postByTitle = postRepository.findPostByTitle(title);
						PostSearchDto post1 = postByTitle.get(0);
						PostSearchDto post2 = postByTitle.get(1);
						//then
						assertThat(postByTitle).size().isEqualTo(2);
						assertThat(post1).extracting("title").isEqualTo(title);
						assertThat(post1).extracting("content").isEqualTo(content);
						assertThat(post2).extracting("title").isEqualTo(title + 2);
						assertThat(post2).extracting("content").isEqualTo(content + 2);

					}
				}
			}

			@DisplayName("내용으로 조회")
			@Nested
			class findPostByContent {
				@DisplayName("성공 케이스")
				@Nested
				class successfulCase {
					@DisplayName("정상 조회 - 데이터 0개")
					@Test
					void successfulCase_NoData() {
						//given
						String example = "NoData";
						//when
						List<PostSearchDto> postByContent = postRepository.findPostByContent(example);
						//then
						assertThat(postByContent).isEmpty();
					}

					@DisplayName("정상 조회 - 데이터 1개")
					@Test
					void successfulCase_SingleData() {
						//given
						//when
						List<PostSearchDto> postByContent = postRepository.findPostByContent(content);
						PostSearchDto postSearchDto = postByContent.get(0);
						//then
						assertThat(postByContent).size().isEqualTo(1);
						assertThat(postSearchDto).extracting("title").isEqualTo(title);
						assertThat(postSearchDto).extracting("content").isEqualTo(content);

					}

					@DisplayName("정상 조회 - 데이터 2개 이상")
					@Test
					void successfulCase_MultiData() {
						//given
						User savedUser = userDataRepository.findById(1L)
							.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저"));

						Post post = Post.builder()
							.title(title + 2)
							.content(content + 2)
							.user(savedUser)
							.build();
						postRepository.save(post);
						//when
						List<PostSearchDto> postByContent = postRepository.findPostByContent(content);
						PostSearchDto post1 = postByContent.get(0);
						PostSearchDto post2 = postByContent.get(1);
						//then
						assertThat(postByContent).size().isEqualTo(2);
						assertThat(post1).extracting("title").isEqualTo(title);
						assertThat(post1).extracting("content").isEqualTo(content);
						assertThat(post2).extracting("title").isEqualTo(title + 2);
						assertThat(post2).extracting("content").isEqualTo(content + 2);

					}
				}
			}

		}

	}

	@DisplayName("포스트 편집")
	@Nested
	class editPost {
		@DisplayName("성공 케이스")
		@Nested
		class successfulCase {
			@DisplayName("부분 변경")
			@Test
			void partialChange() {
				//given
				String changedThumbnailDetail = "changedThumbnailURL";
				//when
				Post editPost = postRepository.findPostById(1L)
					.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포스트"));

				editPost.editPost("", null, changedThumbnailDetail);

				Post editedPost = postRepository.findPostById(1L)
					.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포스트"));
				//then
				assertThat(editedPost).extracting("title").isEqualTo(title);
				assertThat(editedPost).extracting("content").isEqualTo(content);
				assertThat(editedPost).extracting("thumbnail").isEqualTo(changedThumbnailDetail);

			}

			@DisplayName("전체 변경")
			@Test
			void fullChange() {
				//given
				String changedThumbnailDetail = "changedThumbnailURL";
				String changedTitle = "changedTitle";
				String changedContent = "changedContent";
				//when
				Post editPost = postRepository.findPostById(1L)
					.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포스트"));

				editPost.editPost(changedTitle, changedContent, changedThumbnailDetail);

				Post editedPost = postRepository.findPostById(1L)
					.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포스트"));
				//then
				assertThat(editedPost).extracting("title").isEqualTo(changedTitle);
				assertThat(editedPost).extracting("content").isEqualTo(changedContent);
				assertThat(editedPost).extracting("thumbnail").isEqualTo(changedThumbnailDetail);
			}

		}

	}

	@DisplayName("포스트 삭제")
	@Nested
	class removePost {
		@DisplayName("성공 케이스")
		@Nested
		class successfulCase {
			@DisplayName("정상 삭제")
			@Test
			void normalRemove() {
				//given
				String errorMassage = "존재하지 않는 포스트";
				//when
				postRepository.removePostByIdAndUserId(1L, 1L);
				//then
				assertThatThrownBy(() -> {
					postRepository.findPostById(1L)
						.orElseThrow(() -> new IllegalArgumentException(errorMassage));
				})
					.isInstanceOf(IllegalArgumentException.class)
					.hasMessageContaining(errorMassage);

			}

			@DisplayName("비정상 삭제 - 유저와 포스트 작성자 다름")
			@Test
			void abnormalRemove() {
				//given
				User user2 = User.builder()
					.username("user2")
					.email("email2@test.com")
					.password("tester1")
					.build();
				User savedNewUser = userDataRepository.save(user2);
				//when
				postRepository.removePostByIdAndUserId(1L, savedNewUser.getId());

				Post post = postRepository.findPostById(1L)
					.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포스트"));
				//then
				assertThat(post).extracting("title").isEqualTo(title);
				assertThat(post).extracting("content").isEqualTo(content);

			}
		}
	}

}