package io.f12.notionlinkedblog.domain.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CreateCommentDto {
	private String comment;
	private Integer deep;
	private Long parentCommentId;
}
