package io.f12.notionlinkedblog.comments.infrastructure;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import io.f12.notionlinkedblog.comments.service.port.CommentsRepository;
import io.f12.notionlinkedblog.common.config.TestQuerydslConfiguration;
import io.f12.notionlinkedblog.domain.comments.Comments;
import io.f12.notionlinkedblog.domain.post.Post;
import io.f12.notionlinkedblog.domain.user.User;
import io.f12.notionlinkedblog.post.service.port.PostRepository;
import io.f12.notionlinkedblog.user.service.port.UserRepository;

@DataJpaTest
@Import(TestQuerydslConfiguration.class)
class CommentsDataRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private CommentsRepository commentsRepository;

	private User savedUser;
	private Post savedPost;
	private Comments savedComment;

	@BeforeEach
	void init() {
		User user = User.builder()
			.username("tester")
			.email("test@gamil.com")
			.password("1234")
			.build();
		savedUser = userRepository.save(user);

		Post post = Post.builder()
			.user(savedUser)
			.title("testTitle")
			.content("testContent")
			.isPublic(true)
			.build();
		savedPost = postRepository.save(post);
		Comments comments = Comments.builder()
			.user(savedUser)
			.post(savedPost)
			.content("testComment")
			.depth(0)
			.build();
		savedComment = commentsRepository.save(comments);

	}

	@AfterEach
	void clear() {
		commentsRepository.deleteAll();
		postRepository.deleteAll();
		userRepository.deleteAll();
	}

	@DisplayName("PostId로 댓글 조회")
	@Nested
	class GetCommentsWithPostId {
		@DisplayName("성공 케이스")
		@Test
		void successCase() {
			//given
			Comments testComment2 = commentsRepository.save(Comments.builder()
				.user(savedUser)
				.post(savedPost)
				.content("testComment2")
				.depth(0)
				.build());

			//when
			List<Comments> comments = commentsRepository.findByPostId(savedPost.getId());
			Comments comments1 = comments.get(0);
			Comments comments2 = comments.get(1);
			//then
			assertThat(comments).size().isEqualTo(2);
			assertThat(comments1).extracting("content").isEqualTo(savedComment.getContent());
			assertThat(comments2).extracting("content").isEqualTo(testComment2.getContent());
		}
	}

}