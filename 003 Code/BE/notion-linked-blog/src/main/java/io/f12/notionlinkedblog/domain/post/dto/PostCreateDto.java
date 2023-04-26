package io.f12.notionlinkedblog.domain.post.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostCreateDto {

	private String title;
	private String content;
	private String thumbnail;
}
