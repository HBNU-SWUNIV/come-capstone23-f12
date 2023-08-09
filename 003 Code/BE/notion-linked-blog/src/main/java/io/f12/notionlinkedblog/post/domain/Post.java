package io.f12.notionlinkedblog.post.domain;

import java.time.LocalDateTime;
import java.util.List;

import io.f12.notionlinkedblog.comments.domain.Comments;
import io.f12.notionlinkedblog.like.domain.Like;
import io.f12.notionlinkedblog.notion.domain.SyncedPages;
import io.f12.notionlinkedblog.series.domain.Series;
import io.f12.notionlinkedblog.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class Post {
	private Long id;
	private User user;
	private List<Comments> comments;
	private List<Like> likes;
	private Series series;
	private SyncedPages syncedPages;
	private String title;
	private String content;
	private String thumbnailName;
	private String storedThumbnailPath;
	private Long viewCount;
	private Double popularity;
	private String description;
	private Boolean isPublic;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
