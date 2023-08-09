package io.f12.notionlinkedblog.series.domain;

import java.util.List;

import io.f12.notionlinkedblog.post.domain.Post;
import io.f12.notionlinkedblog.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class Series {
	private Long id;
	private User user;
	private List<Post> post;
	private String title;
}
