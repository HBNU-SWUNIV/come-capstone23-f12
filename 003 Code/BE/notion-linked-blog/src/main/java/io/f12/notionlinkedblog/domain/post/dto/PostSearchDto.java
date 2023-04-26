package io.f12.notionlinkedblog.domain.post.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@Builder
public class PostSearchDto {
	private String username;
	private String title;
	private String content;
	private String thumbnail;
	private Long viewCount;

	public PostSearchDto(String username, String title, String content, String thumbnail,
		Long viewCount) {
		this.username = username;
		this.title = title;
		this.content = content;
		this.thumbnail = thumbnail;
		this.viewCount = viewCount;
	}
}
