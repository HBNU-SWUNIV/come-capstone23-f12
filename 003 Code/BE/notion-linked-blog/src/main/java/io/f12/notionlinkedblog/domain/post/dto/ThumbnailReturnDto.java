package io.f12.notionlinkedblog.domain.post.dto;

import org.springframework.core.io.UrlResource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThumbnailReturnDto {
	String thumbnailPath;
	UrlResource image;
}
