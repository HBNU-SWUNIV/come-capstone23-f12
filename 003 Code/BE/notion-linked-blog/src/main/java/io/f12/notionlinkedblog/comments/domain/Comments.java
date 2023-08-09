package io.f12.notionlinkedblog.comments.domain;

import java.time.LocalDateTime;
import java.util.List;

import io.f12.notionlinkedblog.post.domain.Post;
import io.f12.notionlinkedblog.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class Comments {
	private Long id;
	private User user;
	private Post post;
	private String content;
	private Integer depth; // 0 = parents, 1 = children
	private Comments parent;
	private List<Comments> children;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
