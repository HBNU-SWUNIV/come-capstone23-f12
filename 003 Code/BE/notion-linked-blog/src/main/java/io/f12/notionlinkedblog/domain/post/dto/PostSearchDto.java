package io.f12.notionlinkedblog.domain.post.dto;

import io.f12.notionlinkedblog.domain.series.Series;
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
	private Series seriesId;
	private String title;
	private String content;
	private String thumbnail;
	private Long viewCount;

	public PostSearchDto(String username, Series seriesId, String title, String content, String thumbnail,
		Long viewCount) {
		this.username = username;
		this.seriesId = seriesId;
		this.title = title;
		this.content = content;
		this.thumbnail = thumbnail;
		this.viewCount = viewCount;
	}
}
