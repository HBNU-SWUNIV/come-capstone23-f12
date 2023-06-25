package io.f12.notionlinkedblog.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSocialInfoEditDto {
	private String githubLink;
	private String instagramLink;
}
