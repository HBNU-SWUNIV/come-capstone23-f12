package io.f12.notionlinkedblog.comments.domain.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CommentEditDto {
	private Long commentId;
	private String comment;
	private Long parentCommentId;
	private LocalDateTime createdAt;
	private Long authorId;
	private String author;
	private String authorProfileLink;
}
