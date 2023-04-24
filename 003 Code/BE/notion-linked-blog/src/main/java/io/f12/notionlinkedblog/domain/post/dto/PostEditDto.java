package io.f12.notionlinkedblog.domain.post.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class PostEditDto {
	private String title;
	private String content;
	private String thumbnail;

	public PostEditDto(String title, String content, String thumbnail) {
		this.title = title;
		this.content = content;
		this.thumbnail = thumbnail;
	}
}
