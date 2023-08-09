package io.f12.notionlinkedblog.like.domain;

import io.f12.notionlinkedblog.post.domain.Post;
import io.f12.notionlinkedblog.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class Like {
	private Long id;
	private Post post;
	private User user;
}
