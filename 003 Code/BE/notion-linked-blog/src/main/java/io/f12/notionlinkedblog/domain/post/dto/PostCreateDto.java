package io.f12.notionlinkedblog.domain.post.dto;

import io.f12.notionlinkedblog.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostCreateDto {

	private Long id;
	private User user;
	private String title;
	private String content;
	private String thumbnail;
}
