package io.f12.notionlinkedblog.notion.domain;

import io.f12.notionlinkedblog.post.domain.Post;
import io.f12.notionlinkedblog.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class SyncedPages {
	private Long id;
	private String pageId;
	private User user;
	private Post post;
}
