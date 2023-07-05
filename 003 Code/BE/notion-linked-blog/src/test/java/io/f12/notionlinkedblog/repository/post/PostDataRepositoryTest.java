package io.f12.notionlinkedblog.repository.post;

import static io.f12.notionlinkedblog.exceptions.ExceptionMessages.PostExceptionsMessages.*;
import static io.f12.notionlinkedblog.exceptions.ExceptionMessages.UserExceptionsMessages.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import io.f12.notionlinkedblog.config.TestQuerydslConfiguration;
import io.f12.notionlinkedblog.domain.post.Post;
import io.f12.notionlinkedblog.domain.series.Series;
import io.f12.notionlinkedblog.domain.user.User;
import io.f12.notionlinkedblog.repository.series.SeriesDataRepository;
import io.f12.notionlinkedblog.repository.user.UserDataRepository;
import lombok.extern.slf4j.Slf4j;

@DataJpaTest
@Import(TestQuerydslConfiguration.class)
@Slf4j
class PostDataRepositoryTest {

	@Autowired
	private PostDataRepository postDataRepository;
	@Autowired
	private UserDataRepository userDataRepository;
	@Autowired
	private SeriesDataRepository seriesDataRepository;
	@Autowired
	private EntityManager entityManager;

	private User user;
	private Post post;
	private Series series;

	String title = "testTitle";
	String content = "testContent";
	String thumbnail = "testThumbnail";
	String path = "path";

	@BeforeEach
	void init() {
		User savedUser = User.builder()
			.username("tester")
			.email("test@test.com")
			.password("nope")
			.build();
		user = userDataRepository.save(savedUser);

		Series savedSeries = Series.builder()
			.title("testSeries")
			.user(user)
			.post(new ArrayList<>())
			.build();
		series = seriesDataRepository.save(savedSeries);

		Post savedPost = Post.builder()
			.title(title)
			.content(content)
			.user(user)
			.thumbnailName(thumbnail)
			.storedThumbnailPath(path)
			.isPublic(true)
			.build();
		post = postDataRepository.save(savedPost);

	}

	@AfterEach
	void clear() {
		seriesDataRepository.deleteAll();
		postDataRepository.deleteAll();
		userDataRepository.deleteAll();
		entityManager.createNativeQuery("ALTER SEQUENCE user_seq RESTART WITH 1").executeUpdate();
		entityManager.createNativeQuery("ALTER SEQUENCE post_seq RESTART WITH 1").executeUpdate();
	}

	@DisplayName("포스트 조회")
	@Nested
	class PostLookup {

		@DisplayName("단건 조회")
		@Nested
		class SingleSearch {

			@DisplayName("ID로 PostDto 조회")
			@Nested
			class LookupPostDtoById {

				@DisplayName("정상 조회")
				@Test
				void successCase() {
					//given
					//when
					userDataRepository.findById(user.getId())
						.orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
					Post searchPost = postDataRepository.findById(post.getId())
						.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST));

					//then
					assertThat(searchPost).extracting(Post::getTitle).isEqualTo(title);
					assertThat(searchPost).extracting(Post::getContent).isEqualTo(content);
				}

				@DisplayName("실패 케이스")
				@Nested
				class FailCase {
					@DisplayName("비정상 조회 - 없는 데이터 조회시")
					@Test
					void searchUnavailablePost() {
						//given
						String title = "testTitle";
						String content = "testContent";

						//when
						User savedUser = userDataRepository.findById(user.getId())
							.orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));

						Post post = Post.builder()
							.title(title)
							.content(content)
							.user(savedUser)
							.isPublic(true)
							.build();
						Post save = postDataRepository.save(post);
						long searchId = save.getId() + 1;

						//then
						Optional<Post> postById = postDataRepository.findById(searchId);
						assertThatThrownBy(() -> {
							postById.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST));
						}).isInstanceOf(IllegalArgumentException.class)
							.hasMessageContaining(POST_NOT_EXIST);

					}
				}
			}

			@DisplayName("ID로 Post 조회")
			@Nested
			class LookupPostById {

				@DisplayName("정상 조회")
				@Test
				void successfulCase() {
					//given
					//when
					Post searchPostById = postDataRepository.findById(post.getId())
						.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST));
					//then
					assertThat(searchPostById).extracting(Post::getTitle).isEqualTo(title);
					assertThat(searchPostById).extracting(Post::getContent).isEqualTo(content);

				}

				@DisplayName("실패 케이스")
				@Nested
				class FaileCase {

					@DisplayName("존재하지 않는 포스트 조회")
					@Test
					void searchUnavailablePost() {
						//given
						Long postId = post.getId() + 1L;
						//when
						//then
						assertThatThrownBy(() -> {
							postDataRepository.findById(postId)
								.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST));
						}).isInstanceOf(IllegalArgumentException.class)
							.hasMessageContaining(POST_NOT_EXIST);
					}

				}
			}

		}

		@DisplayName("다건 조회")
		@Nested
		class MultiSearch {
			@DisplayName("제목으로 조회")
			@Nested
			class LookupPostsByTitle {

				@DisplayName("성공 케이스")
				@Nested
				class SuccessCase {
					@DisplayName("정상 조회 - 데이터 0개")
					@Test
					void successfulCase_NoData() {
						//given
						String example = "NoData";
						PageRequest paging = PageRequest.of(0, 20);
						//when
						List<Long> ids = postDataRepository.findPostIdsByTitle(example, paging);
						List<Post> posts = postDataRepository.findByPostIdsJoinWithUserAndLikeOrderByTrend(ids);
						//then
						assertThat(posts).isEmpty();

					}

					@DisplayName("정상 조회 - 데이터 1개")
					@Test
					void successfulCase_SingleData() {
						//given
						PageRequest paging = PageRequest.of(0, 20);
						//when
						List<Long> ids = postDataRepository.findPostIdsByTitle(title, paging);
						List<Post> posts = postDataRepository.findByPostIdsJoinWithUserAndLikeOrderByTrend(ids);
						Post post = posts.get(0);

						//then
						assertThat(posts).size().isEqualTo(1);
						assertThat(post).extracting("title").isEqualTo(title);
						assertThat(post).extracting("content").isEqualTo(content);

					}

					@DisplayName("정상 조회 - paging 갯수 이상")
					@Test
					void successfulCase_MultiData() {
						//given
						User savedUser = userDataRepository.findById(user.getId())
							.orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
						for (int i = 0; i < 30; i++) {
							Post savedPost = Post.builder()
								.title(title + i)
								.content(content)
								.user(user)
								.isPublic(true)
								.build();
							postDataRepository.save(savedPost);
						}
						PageRequest paging1 = PageRequest.of(0, 20);
						PageRequest paging2 = PageRequest.of(1, 20);

						//when
						List<Long> ids1 = postDataRepository.findPostIdsByTitle(title, paging1);
						List<Post> posts1 = postDataRepository.findByPostIdsJoinWithUserAndLikeOrderByLatest(ids1);

						List<Long> ids2 = postDataRepository.findPostIdsByTitle(title, paging2);
						List<Post> posts2 = postDataRepository.findByPostIdsJoinWithUserAndLikeOrderByLatest(ids2);
						//then
						assertThat(posts1).size().isEqualTo(20);
						assertThat(posts2).size().isEqualTo(11);

					}
				}
			}

			@DisplayName("내용으로 조회")
			@Nested
			class LookupPostByContent {
				@DisplayName("성공 케이스")
				@Nested
				class SuccessCase {
					@DisplayName("정상 조회 - 데이터 0개")
					@Test
					void successfulCase_NoData() {
						//given
						String example = "NoData";
						PageRequest paging = PageRequest.of(0, 20);
						//when
						// Slice<Post> postByContent = postDataRepository.findByContent(example, paging);
						List<Long> ids = postDataRepository.findPostIdsByContent(example, paging);
						List<Post> posts = postDataRepository.findByPostIdsJoinWithUserAndLikeOrderByTrend(ids);
						//then
						assertThat(posts).isEmpty();
					}

					@DisplayName("정상 조회 - 데이터 1개")
					@Test
					void successfulCase_SingleData() {
						//given
						PageRequest paging = PageRequest.of(0, 20);
						//when

						List<Long> ids = postDataRepository.findPostIdsByContent(content, paging);
						List<Post> posts = postDataRepository.findByPostIdsJoinWithUserAndLikeOrderByTrend(ids);
						Post post = posts.get(0);

						//then
						assertThat(posts).size().isEqualTo(1);
						assertThat(post).extracting("title").isEqualTo(title);
						assertThat(post).extracting("content").isEqualTo(content);
					}

					@DisplayName("정상 조회 - paging 갯수 이상")
					@Test
					void successfulCase_MultiData() {
						//given
						User savedUser = userDataRepository.findById(user.getId())
							.orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));

						for (int i = 0; i < 30; i++) {
							Post savedPost = Post.builder()
								.title(title + i)
								.content(content + i)
								.user(user)
								.isPublic(true)
								.build();
							postDataRepository.save(savedPost);
						}
						PageRequest paging1 = PageRequest.of(0, 20);
						PageRequest paging2 = PageRequest.of(1, 20);
						//when
						List<Long> ids1 = postDataRepository.findPostIdsByContent(content, paging1);
						List<Post> posts1 = postDataRepository.findByPostIdsJoinWithUserAndLikeOrderByLatest(ids1);

						List<Long> ids2 = postDataRepository.findPostIdsByContent(content, paging2);
						List<Post> posts2 = postDataRepository.findByPostIdsJoinWithUserAndLikeOrderByLatest(ids2);

						//then
						assertThat(posts1).size().isEqualTo(20);
						assertThat(posts2).size().isEqualTo(11);

					}
				}
			}
		}

		@DisplayName("포스트 정렬 조회")
		@Nested
		class SortByList {
			@BeforeEach
			void init() {
				for (int i = 0; i < 10; i++) {
					Post newPost = Post.builder()
						.title(title + " " + i)
						.content(content)
						.user(user)
						.isPublic(true)
						.createdAt(LocalDateTime.of(2023, 1, i + 1, 0, 0))
						.build();
					Post save = postDataRepository.save(newPost);
					// log.info("save.isEmpty: {}", save.getId());
					series.addPost(save);
				}
				// entityManager.merge(series);
			}

			@DisplayName("최신순 조회")
			@Nested
			class LookupByDate {
				@DisplayName("성공 케이스")
				@Nested
				class SuccessCase {
					@DisplayName("조회 성공")
					@Test
					void successful() {
						//given
						PageRequest paging = PageRequest.of(0, 3);
						//when
						List<Long> ids = postDataRepository.findLatestPostIdsByCreatedAtDesc(paging);
						List<Post> posts = postDataRepository.findByPostIdsJoinWithUserAndLikeOrderByLatest(ids);
						//then
						assertThat(posts).size().isEqualTo(paging.getPageSize());
					}
				}
			}

			@DisplayName("인기순 조회")
			@Nested
			class LookupByPopularity {
				@DisplayName("성공 케이스")
				@Nested
				class SuccessCase {
					@DisplayName("조회 성공")
					@Test
					void successful() {
						//given
						PageRequest paging = PageRequest.of(0, 3);
						//when
						List<Long> ids = postDataRepository.findPopularityPostIdsByViewCountAtDesc(paging);
						List<Post> posts = postDataRepository.findByPostIdsJoinWithUserAndLikeOrderByTrend(ids);
						//then
						assertThat(posts).size().isEqualTo(paging.getPageSize());
					}
				}
			}

			@DisplayName("시리즈 Id로 조회")
			@Nested
			class LookupBySeriesId {
				@DisplayName("오름차순")
				@Nested
				class LookupAsc {
					@DisplayName("성공 케이스")
					@Nested
					class SuccessCase {
						@DisplayName("조회 성공")
						@Test
						void successful() {
							//given
							PageRequest paging = PageRequest.of(0, 3);
							//when
							List<Long> ids = postDataRepository.findIdsBySeriesIdAsc(series.getId(), paging);
							List<Post> posts = postDataRepository.findByIdsJoinWithSeries(ids);
							//then
							assertThat(posts).size().isEqualTo(paging.getPageSize());
						}
					}
				}

				@DisplayName("내림차순")
				@Nested
				class LookupDesc {
					@DisplayName("성공 케이스")
					@Nested
					class SuccessCase {
						@DisplayName("조회 성공")
						@Test
						void successful() {
							//given
							PageRequest paging = PageRequest.of(0, 3);
							//when
							List<Long> ids = postDataRepository.findIdsBySeriesIdDesc(series.getId(), paging);
							List<Post> posts = postDataRepository.findByIdsJoinWithSeries(ids);
							//then
							assertThat(posts).size().isEqualTo(paging.getPageSize());
						}
					}

				}
			}

		}

	}

	@DisplayName("포스트 편집")
	@Nested
	class EditPost {
		@DisplayName("성공 케이스")
		@Nested
		class SuccessCase {
			@DisplayName("미 변경")
			@Test
			void partialChange() {
				//given
				//when
				Post editPost = postDataRepository.findById(post.getId())
					.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST));

				editPost.editPost("", null);

				Post editedPost = postDataRepository.findById(post.getId())
					.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST));
				//then
				assertThat(editedPost).extracting("title").isEqualTo(title);
				assertThat(editedPost).extracting("content").isEqualTo(content);

			}

			@DisplayName("전체 변경")
			@Test
			void fullChange() {
				//given
				String changedTitle = "changedTitle";
				String changedContent = "changedContent";
				//when
				Post editPost = postDataRepository.findById(post.getId())
					.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST));

				editPost.editPost(changedTitle, changedContent);

				Post editedPost = postDataRepository.findById(post.getId())
					.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST));
				//then
				assertThat(editedPost).extracting("title").isEqualTo(changedTitle);
				assertThat(editedPost).extracting("content").isEqualTo(changedContent);
			}

		}

	}

	@DisplayName("썸네일 조회")
	@Nested
	class LookupThumbnailImage {
		@DisplayName("ID로 PostDto 조회")
		@Nested
		class LookupThumbnailPathByThumbnailName {
			@DisplayName("정상 조회")
			@Test
			void successCase() {
				//given

				//when
				String thumbnailPathWithName = postDataRepository.findThumbnailPathWithName(thumbnail);
				//then
				assertThat(thumbnailPathWithName).isEqualTo(path);

			}
		}
	}

}