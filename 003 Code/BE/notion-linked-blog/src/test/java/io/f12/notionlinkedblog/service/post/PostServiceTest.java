package io.f12.notionlinkedblog.service.post;

import static io.f12.notionlinkedblog.exceptions.ExceptionMessages.PostExceptionsMessages.*;
import static io.f12.notionlinkedblog.exceptions.ExceptionMessages.UserExceptionsMessages.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import io.f12.notionlinkedblog.domain.likes.dto.LikeSearchDto;
import io.f12.notionlinkedblog.domain.post.Post;
import io.f12.notionlinkedblog.domain.post.dto.PostEditDto;
import io.f12.notionlinkedblog.domain.post.dto.PostSearchDto;
import io.f12.notionlinkedblog.domain.post.dto.PostSearchResponseDto;
import io.f12.notionlinkedblog.domain.post.dto.SearchRequestDto;
import io.f12.notionlinkedblog.domain.user.User;
import io.f12.notionlinkedblog.repository.like.LikeDataRepository;
import io.f12.notionlinkedblog.repository.post.PostDataRepository;
import io.f12.notionlinkedblog.repository.user.UserDataRepository;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

	@InjectMocks
	PostService postService;

	@Mock
	PostDataRepository postDataRepository;

	@Mock
	UserDataRepository userDataRepository;
	@Mock
	LikeDataRepository likeDataRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@DisplayName("포스트 생성")
	@Nested
	class CreatePost {

		@DisplayName("성공케이스")
		@Nested
		class SuccessCase {
			@DisplayName("모든 데이터 존재")
			@Test
			void haveEveryData() throws IOException {
				//given
				Long fakeId = 1L;
				User user = User.builder()
					.username("tester")
					.email("test@test.com")
					.password("test123")
					.build();

				String title = "testTitle";
				String content = "testContent";
				String thumbnail = "testThumbnail";
				String path = "path";
				String description = "description";
				String isPublic = "0";

				Post returnPost = Post.builder()
					.user(user)
					.title(title)
					.content(content)
					.thumbnailName(thumbnail)
					.storedThumbnailPath(path)
					.build();

				ReflectionTestUtils.setField(user, "id", fakeId);

				File file = new ClassPathResource("static/images/test.jpg").getFile();

				//Mock
				MultipartFile mockMultipartFile = new MockMultipartFile(file.getName(), new FileInputStream(file));
				given(userDataRepository.findById(fakeId))
					.willReturn(Optional.of(user));
				given(postDataRepository.save(any(Post.class)))
					.willReturn(returnPost);

				//when
				PostSearchDto createdPost = postService.createPost(fakeId, title, content, description, isPublic,
					mockMultipartFile);

				//then
				assertThat(createdPost).extracting("title").isEqualTo(title);
				assertThat(createdPost).extracting("content").isEqualTo(content);
			}

			@DisplayName("섬네일 제외")
			@Test
			void withoutThumbnail() throws IOException {
				//given
				Long fakeId = 1L;
				User user = User.builder()
					.username("tester")
					.email("test@test.com")
					.password("test123")
					.build();

				String title = "testTitle";
				String content = "testContent";
				String description = "description";
				String isPublic = "0";

				Post returnPost = Post.builder()
					.user(user)
					.title(title)
					.content(content)
					.build();

				ReflectionTestUtils.setField(user, "id", fakeId);
				//Mock
				given(userDataRepository.findById(fakeId))
					.willReturn(Optional.of(user));
				given(postDataRepository.save(any(Post.class)))
					.willReturn(returnPost);

				//when
				PostSearchDto createdPost = postService.createPost(fakeId, title, content, description, isPublic,
					null);
				//then
				assertThat(createdPost).extracting("title").isEqualTo(title);
				assertThat(createdPost).extracting("content").isEqualTo(content);
				assertThat(createdPost).extracting("requestThumbnailLink").isNull();
			}

		}

		@DisplayName("실패 케이스")
		@Nested
		class FailCase {
			@DisplayName("USER 미존재")
			@Test
			void undefinedUser() {
				//given
				String title = "testTitle";
				String content = "testContent";
				String description = "description";
				String isPublic = "1";
				Long fakeId = 1L;

				//Mock
				given(userDataRepository.findById(fakeId))
					.willReturn(null);

				//when
				//then
				assertThatThrownBy(() -> {
					postService.createPost(fakeId, title, content, description, isPublic, null);
				}).isInstanceOf(NullPointerException.class);

			}

		}

	}

	@DisplayName("포스트 조회")
	@Nested
	class LookupPost {

		@DisplayName("title 로 조회")
		@Nested
		class LookupPostByTitle {
			@DisplayName("성공케이스")
			@Test
			void successCase() {
				//given
				String title = "testTitle";
				String content = "testContent";
				String thumbnail = "testThumbnail";
				String username = "tester";
				String path = "path";

				User user = User.builder()
					.username(username)
					.email("test@gamil.com")
					.password(passwordEncoder.encode("1234"))
					.build();

				Long fakePostAId = 1L;
				Long fakePostBId = 2L;
				Post post1 = Post.builder()
					.user(user)
					.title(title)
					.content(content)
					.thumbnailName(thumbnail)
					.storedThumbnailPath(path)
					.build();
				Post post2 = Post.builder()
					.user(user)
					.title(title)
					.content(content)
					.build();
				ReflectionTestUtils.setField(post1, "id", fakePostAId);
				ReflectionTestUtils.setField(post2, "id", fakePostBId);
				List<Long> ids = new ArrayList<>();
				ids.add(fakePostAId);
				ids.add(fakePostBId);
				List<Post> postList = new ArrayList<>();
				postList.add(post1);
				postList.add(post2);

				SearchRequestDto requestDto = SearchRequestDto.builder()
					.param("test")
					.pageNumber(0)
					.build();
				PageRequest paging = PageRequest.of(requestDto.getPageNumber(), 20);
				//Mock
				given(postDataRepository.findPostIdsByTitle(requestDto.getParam(), paging))
					.willReturn(ids);
				given(postDataRepository.findByIds(ids))
					.willReturn(postList);
				//when
				PostSearchResponseDto posts = postService.getPostsByTitle(requestDto);
				PostSearchDto postSearchDto = posts.getPosts().get(0);
				//then
				assertThat(posts).extracting(PostSearchResponseDto::getPageSize).isEqualTo(20);
				assertThat(posts).extracting(PostSearchResponseDto::getPageNow).isEqualTo(requestDto.getPageNumber());
				assertThat(posts).extracting(PostSearchResponseDto::getElementsSize).isEqualTo(2);
				assertThat(posts.getPosts()).size().isEqualTo(2);
				assertThat(postSearchDto).extracting("title").isEqualTo(title);
				assertThat(postSearchDto).extracting("author").isEqualTo(username);
			}
		}

		@DisplayName("content 로 조회")
		@Nested
		class LookupPostByContent {
			@DisplayName("성공케이스")
			@Test
			void successCase() {

				//given
				String title = "testTitle";
				String content = "testContent";
				String thumbnail = "testThumbnail";
				String username = "tester";
				String path = "path";

				User user = User.builder()
					.username(username)
					.email("test@gamil.com")
					.password(passwordEncoder.encode("1234"))
					.build();

				SearchRequestDto requestDto = SearchRequestDto.builder()
					.param("test")
					.pageNumber(0)
					.build();

				Long fakePostAId = 1L;
				Long fakePostBId = 2L;
				Post post1 = Post.builder()
					.user(user)
					.title(title)
					.content(content)
					.thumbnailName(thumbnail)
					.storedThumbnailPath(path)
					.build();
				Post post2 = Post.builder()
					.user(user)
					.title(title)
					.content(content)
					.build();

				ReflectionTestUtils.setField(post1, "id", fakePostAId);
				ReflectionTestUtils.setField(post2, "id", fakePostBId);
				List<Long> ids = new ArrayList<>();
				ids.add(fakePostAId);
				ids.add(fakePostBId);
				List<Post> postList = new ArrayList<>();
				postList.add(post1);
				postList.add(post2);

				PageRequest paging = PageRequest.of(requestDto.getPageNumber(), 20);

				//Mock
				given(postDataRepository.findPostIdsByContent(requestDto.getParam(), paging))
					.willReturn(ids);
				given(postDataRepository.findByIds(ids))
					.willReturn(postList);
				//when
				PostSearchResponseDto posts = postService.getPostByContent(requestDto);
				PostSearchDto postSearchDto = posts.getPosts().get(0);
				//then
				assertThat(posts).extracting(PostSearchResponseDto::getPageSize).isEqualTo(20);
				assertThat(posts).extracting(PostSearchResponseDto::getPageNow).isEqualTo(requestDto.getPageNumber());
				assertThat(posts).extracting(PostSearchResponseDto::getElementsSize).isEqualTo(2);
				assertThat(posts.getPosts()).size().isEqualTo(2);
				assertThat(postSearchDto).extracting("title").isEqualTo(title);
				assertThat(postSearchDto).extracting("author").isEqualTo(username);

			}
		}

		@DisplayName("postId 로 조회")
		@Nested
		class LookupPostByPostId {
			@DisplayName("성공케이스")
			@Test
			void successCase() {
				//given
				Long fakeId = 1L;
				String title = "testTitle";
				String content = "testContent";
				String thumbnail = "testThumbnail";
				String username = "tester";
				String path = "path";

				User user = User.builder()
					.username(username)
					.email("test@gamil.com")
					.password(passwordEncoder.encode("1234"))
					.build();

				Post testPost = Post.builder()
					.user(user)
					.title(title)
					.content(content)
					.thumbnailName(thumbnail)
					.storedThumbnailPath(path)
					.viewCount(10L)
					.build();
				//Mock
				given(postDataRepository.findById(fakeId))
					.willReturn(Optional.ofNullable(testPost));
				//when
				PostSearchDto postDto = postService.getPostDtoById(fakeId);

				//then
				assertThat(postDto).extracting("title").isEqualTo(title);
				assertThat(postDto).extracting("author").isEqualTo(username);

			}

			@DisplayName("실패케이스 - 해당 포스트 미존재")
			@Test
			void failureCase() {
				//given
				Long fakeId = 1L;

				//Mock
				given(postDataRepository.findById(fakeId))
					.willReturn(Optional.empty());
				//when
				//then
				assertThatThrownBy(() -> {
					postService.getPostDtoById(fakeId);
				}).isInstanceOf(IllegalArgumentException.class)
					.hasMessageContaining(POST_NOT_EXIST);
			}
		}

		@DisplayName("최신 포스트 조회")
		@Nested
		class LookupLatestPosts {
			@DisplayName("성공케이스")
			@Test
			void successCase() {
				//given
				Long fakeUserId = 1L;
				User user = User.builder()
					.email("test@gmail.com")
					.username("tester")
					.password(passwordEncoder.encode("1234"))
					.build();
				ReflectionTestUtils.setField(user, "id", fakeUserId);

				Long fakePostAId = 1L;
				Long fakePostBId = 2L;
				Post postA = Post.builder()
					.user(user)
					.title("testTitle")
					.content("testContent")
					.thumbnailName("thumbnail")
					.storedThumbnailPath("path")
					.user(user)
					.build();
				Post postB = Post.builder()
					.user(user)
					.title("testTitle")
					.content("testContent")
					.user(user)
					.build();
				ReflectionTestUtils.setField(postA, "id", fakePostAId);
				ReflectionTestUtils.setField(postB, "id", fakePostBId);

				Integer requestPageNumber = 0;
				PageRequest paging = PageRequest.of(requestPageNumber, 20);

				List<Long> postIds = new ArrayList<>();
				postIds.add(fakePostAId);
				postIds.add(fakePostBId);
				List<Post> postList = new ArrayList<>();
				postList.add(postA);
				postList.add(postB);

				//Mock
				given(postDataRepository.findLatestPostIdsByCreatedAtDesc(paging))
					.willReturn(postIds);
				given(postDataRepository.findByIds(postIds))
					.willReturn(postList);

				//when
				PostSearchResponseDto latestPosts = postService.getLatestPosts(requestPageNumber);
				//then
				assertThat(latestPosts).extracting(PostSearchResponseDto::getPageSize).isEqualTo(20);
				assertThat(latestPosts).extracting(PostSearchResponseDto::getPageNow).isEqualTo(requestPageNumber);
				assertThat(latestPosts).extracting(PostSearchResponseDto::getElementsSize).isEqualTo(2);
				assertThat(latestPosts.getPosts()).size().isEqualTo(2);
			}
		}

		@DisplayName("인기 포스트 조회")
		@Nested
		class LookupTrendPosts {
			@DisplayName("성공케이스")
			@Test
			void successCase() {
				//given
				Long fakeUserId = 1L;
				User user = User.builder()
					.email("test@gmail.com")
					.username("tester")
					.password(passwordEncoder.encode("1234"))
					.build();
				ReflectionTestUtils.setField(user, "id", fakeUserId);

				Long fakePostAId = 1L;
				Long fakePostBId = 2L;
				Post postA = Post.builder()
					.user(user)
					.title("testTitle")
					.content("testContent")
					.user(user)
					.build();
				Post postB = Post.builder()
					.user(user)
					.title("testTitle")
					.content("testContent")
					.user(user)
					.build();
				ReflectionTestUtils.setField(postA, "id", fakePostAId);
				ReflectionTestUtils.setField(postB, "id", fakePostBId);

				Integer requestPageNumber = 0;
				PageRequest paging = PageRequest.of(requestPageNumber, 20);

				List<Long> postIds = new ArrayList<>();
				postIds.add(fakePostAId);
				postIds.add(fakePostBId);
				List<Post> postList = new ArrayList<>();
				postList.add(postA);
				postList.add(postB);

				//Mock
				given(postDataRepository.findPopularityPostIdsByViewCountAtDesc(paging))
					.willReturn(postIds);
				given(postDataRepository.findByIds(postIds))
					.willReturn(postList);

				//when
				PostSearchResponseDto latestPosts = postService.getPopularityPosts(requestPageNumber);
				//then
				assertThat(latestPosts).extracting(PostSearchResponseDto::getPageSize).isEqualTo(20);
				assertThat(latestPosts).extracting(PostSearchResponseDto::getPageNow).isEqualTo(requestPageNumber);
				assertThat(latestPosts).extracting(PostSearchResponseDto::getElementsSize).isEqualTo(2);
				assertThat(latestPosts.getPosts()).size().isEqualTo(2);
			}
		}
	}

	@DisplayName("포스트 삭제")
	@Nested
	class RemovePost {
		@DisplayName("성공 케이스")
		@Test
		void successfulCase() {
			//given
			Long fakeUserId = 1L;
			Long fakePostId = 1L;
			User user = User.builder()
				.email("test@gmail.com")
				.username("tester")
				.password(passwordEncoder.encode("1234"))
				.build();

			Post returnPost = Post.builder()
				.user(User.builder().username("tester").email("test@test.com").password("password").build())
				.title("testTitle")
				.content("testContent")
				.user(user)
				.build();
			ReflectionTestUtils.setField(user, "id", fakeUserId);
			ReflectionTestUtils.setField(returnPost, "id", fakePostId);
			//Mock
			given(postDataRepository.findById(fakePostId))
				.willReturn(Optional.ofNullable(returnPost));
			//when
			postService.removePost(fakePostId, fakeUserId);
		}

		@DisplayName("실패 케이스 - post 미존재")
		@Test
		void failureCase() {
			//given
			Long fakeUserId = 1L;
			Long fakePostId = 1L;

			//Mock
			given(postDataRepository.findById(fakePostId))
				.willReturn(Optional.empty());
			//when
			//then
			assertThatThrownBy(() -> {
				postService.removePost(fakePostId, fakeUserId);
			}).isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining(POST_NOT_EXIST);

		}

	}

	@DisplayName("포스트 수정")
	@Nested
	class EditPost {

		@DisplayName("성공케이스")
		@Nested
		class SuccessCase {
			@DisplayName("데이터 수정")
			@Test
			void editEveryData() {
				//given
				Long fakePostId = 1L;
				Long fakeUserId = 1L;
				String editTitle = "editedTitle";
				String editContent = "editedContent";

				PostEditDto editDto = PostEditDto.builder()
					.title(editTitle)
					.content(editContent)
					.build();

				User user = User.builder()
					.username("tester")
					.email("test@test.com")
					.password("password")
					.build();
				ReflectionTestUtils.setField(user, "id", fakeUserId);
				Post returnPost = Post.builder()
					.user(user)
					.title("testTitle")
					.content("testContent")
					.build();

				//Mock
				given(postDataRepository.findById(fakePostId))
					.willReturn(Optional.ofNullable(returnPost));
				//when
				postService.editPostContent(fakePostId, fakeUserId, editDto);

			}
		}

		@DisplayName("실패케이스")
		@Nested
		class FailCase {

			@DisplayName("포스트 미존재")
			@Test
			void undefinedPost() {
				//given
				Long fakePostId = 1L;
				Long fakeUserId = 1L;
				String editTitle = "editedTitle";
				String editContent = "editedContent";
				PostEditDto editDto = PostEditDto.builder()
					.title(editTitle)
					.content(editContent)
					.build();
				//Mock
				given(postDataRepository.findById(fakePostId))
					.willReturn(Optional.empty());
				//when
				//then
				assertThatThrownBy(() -> {
					postService.editPostContent(fakePostId, fakeUserId, editDto);
				}).isInstanceOf(IllegalArgumentException.class)
					.hasMessageContaining(POST_NOT_EXIST);

			}

			@DisplayName("작성자와 편집자 미일치")
			@Test
			void unMatchingWriterAndEditor() {
				//given
				Long fakePostId = 1L;
				Long fakeUserId = 1L;
				Long illegalEditorId = fakeUserId + 1L;
				String editTitle = "editedTitle";
				String editContent = "editedContent";

				PostEditDto editDto = PostEditDto.builder()
					.title(editTitle)
					.content(editContent)
					.build();

				User writer = User.builder()
					.username("tester")
					.email("test@test.com")
					.password("password")
					.build();

				ReflectionTestUtils.setField(writer, "id", fakeUserId);

				Post returnPost = Post.builder()
					.user(writer)
					.title("testTitle")
					.content("testContent")
					.build();

				//Mock
				given(postDataRepository.findById(fakePostId))
					.willReturn(Optional.ofNullable(returnPost));
				//when
				//then
				assertThatThrownBy(() -> {
					postService.editPostContent(fakePostId, illegalEditorId, editDto);
				}).isInstanceOf(IllegalStateException.class)
					.hasMessageContaining(WRITER_USER_NOT_MATCH);

			}

		}
	}

	@DisplayName("포스트 좋아요")
	@Nested
	class LikePost {
		@DisplayName("성공 케이스")
		@Nested
		class SuccessCase {
			@DisplayName("좋아요")
			@Test
			void likeTest() {
				//given
				Long fakeUserId = 1L;
				User user = User.builder()
					.username("tester")
					.email("test@gmail.com")
					.password("1234")
					.build();
				ReflectionTestUtils.setField(user, "id", fakeUserId);

				Long fakePostId = 1L;
				Post post = Post.builder()
					.user(user)
					.title("testTitle")
					.content("testContent")
					.build();
				ReflectionTestUtils.setField(post, "id", fakePostId);
				//mock
				given(postDataRepository.findById(fakePostId))
					.willReturn(Optional.of(post));
				given(userDataRepository.findById(fakeUserId))
					.willReturn(Optional.of(user));
				given(likeDataRepository.findByUserIdAndPostId(fakeUserId, fakePostId))
					.willReturn(Optional.empty());
				//when
				postService.likeStatusChange(fakePostId, fakeUserId);
			}

			@DisplayName("좋아요 취소")
			@Test
			void cancelLikeTest() {
				//given
				Long fakeUserId = 1L;
				User user = User.builder()
					.username("tester")
					.email("test@gmail.com")
					.password("1234")
					.build();
				ReflectionTestUtils.setField(user, "id", fakeUserId);

				Long fakePostId = 1L;
				Post post = Post.builder()
					.user(user)
					.title("testTitle")
					.content("testContent")
					.build();
				ReflectionTestUtils.setField(post, "id", fakePostId);
				LikeSearchDto dto = LikeSearchDto.builder()
					.postId(post.getId())
					.userID(user.getId())
					.likeId(1L)
					.build();
				//mock
				given(postDataRepository.findById(fakePostId))
					.willReturn(Optional.of(post));
				given(userDataRepository.findById(fakeUserId))
					.willReturn(Optional.of(user));
				given(likeDataRepository.findByUserIdAndPostId(fakeUserId, fakePostId))
					.willReturn(Optional.of(dto));
				//when
				postService.likeStatusChange(fakePostId, fakeUserId);
			}
		}

		@DisplayName("실패 케이스")
		@Nested
		class FailCase {
			@DisplayName("회원 미존재")
			@Test
			void noExistUser() {
				//given
				Long fakeUserId = 1L;
				User user = User.builder()
					.username("tester")
					.email("test@gmail.com")
					.password("1234")
					.build();

				Long fakePostId = 1L;
				Post post = Post.builder()
					.user(user)
					.title("testTitle")
					.content("testContent")
					.build();
				//mock
				given(postDataRepository.findById(fakePostId))
					.willReturn(Optional.of(post));
				given(userDataRepository.findById(fakeUserId))
					.willReturn(Optional.empty());
				//when
				assertThatThrownBy(() -> {
					postService.likeStatusChange(fakePostId, fakeUserId);
				})
					.isInstanceOf(IllegalArgumentException.class)
					.hasMessageContaining(USER_NOT_EXIST);

			}

			@DisplayName("포스트 미존재")
			@Test
			void noExistPost() {
				//given
				Long fakeUserId = 1L;
				User user = User.builder()
					.username("tester")
					.email("test@gmail.com")
					.password("1234")
					.build();

				Long fakePostId = 1L;
				Post post = Post.builder()
					.user(user)
					.title("testTitle")
					.content("testContent")
					.build();
				//mock
				given(postDataRepository.findById(fakePostId))
					.willReturn(Optional.empty());
				//when
				assertThatThrownBy(() -> {
					postService.likeStatusChange(fakePostId, fakeUserId);
				})
					.isInstanceOf(IllegalArgumentException.class)
					.hasMessageContaining(POST_NOT_EXIST);

			}
		}

	}

	@DisplayName("썸네일 조회")
	@Nested
	class ThumbnailLookup {

		@DisplayName("성공 케이스")
		@Nested
		class SuccessCase {
			@DisplayName("썸네일 존재 케이스")
			@Test
			void thumbnailExist() throws MalformedURLException {
				//given
				String imageName = "thumbName";
				User user = User.builder()
					.username("tester")
					.email("test@gmail.com")
					.password("1234")
					.build();
				Post post = Post.builder()
					.user(user)
					.title("testTitle")
					.content("testContent")
					.thumbnailName(imageName)
					.storedThumbnailPath("path.png")
					.build();
				//mock
				given(postDataRepository.findThumbnailPathWithName(imageName))
					.willReturn("path.png");
				//when
				ResponseEntity<Resource> resourceResponseEntity = postService.readImageFile(imageName);

				//then
				assertThat(resourceResponseEntity).extracting("statusCode").isEqualTo(HttpStatus.OK);
				assertThat(resourceResponseEntity.getHeaders().getContentType()).isEqualTo(MediaType.IMAGE_PNG);
			}
		}

		@DisplayName("실패 케이스")
		@Nested
		class FailureCase {
			@DisplayName("썸네일 존재 케이스")
			@Test
			void thumbnailExist() {
				//given
				String imageName = "thumbName";
				User user = User.builder()
					.username("tester")
					.email("test@gmail.com")
					.password("1234")
					.build();
				Post post = Post.builder()
					.user(user)
					.title("testTitle")
					.content("testContent")
					.build();
				//when
				//then
				assertThatThrownBy(() -> {
					ResponseEntity<Resource> resourceResponseEntity = postService.readImageFile(imageName);
				}).isInstanceOf(IllegalArgumentException.class);
			}
		}

	}
}