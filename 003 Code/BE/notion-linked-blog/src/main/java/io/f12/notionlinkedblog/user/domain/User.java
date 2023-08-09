package io.f12.notionlinkedblog.user.domain;

import java.time.LocalDateTime;
import java.util.List;

import io.f12.notionlinkedblog.comments.domain.Comments;
import io.f12.notionlinkedblog.like.domain.Like;
import io.f12.notionlinkedblog.notion.domain.SyncedPages;
import io.f12.notionlinkedblog.oauth.domain.notion.NotionOauth;
import io.f12.notionlinkedblog.post.domain.Post;
import io.f12.notionlinkedblog.series.domain.Series;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class User {
	private Long id;
	private List<Post> posts;
	private List<Comments> comments;
	private List<Like> likes;
	private List<Series> series;
	private NotionOauth notionOauth;
	private List<SyncedPages> syncedPages;
	private String username;
	private String email;
	private String password;
	private String profile;
	private String introduction;
	private String blogTitle;
	private String githubLink;
	private String instagramLink;
	private LocalDateTime createdAt;
}
