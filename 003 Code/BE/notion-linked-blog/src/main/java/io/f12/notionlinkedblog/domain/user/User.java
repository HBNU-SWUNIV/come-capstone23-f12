package io.f12.notionlinkedblog.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.util.StringUtils;

import io.f12.notionlinkedblog.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "users")
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String username;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String password;
	private String profile;
	private String introduction;
	private String blogTitle;
	private String githubLink;
	private String instagramLink;

	@Builder
	public User(Long id, String username, String email, String password, String profile, String introduction,
		String blogTitle, String githubLink, String instagramLink) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.profile = profile;
		this.introduction = introduction;
		this.blogTitle = blogTitle;
		this.githubLink = githubLink;
		this.instagramLink = instagramLink;
	}

	public void editProfile(User user) {
		if (StringUtils.hasText(user.getUsername())) {
			this.username = user.getUsername();
		}
		if (StringUtils.hasText(user.getEmail())) {
			this.email = user.getEmail();
		}
		if (StringUtils.hasText(user.getPassword())) {
			this.password = user.getPassword();
		}
		if (StringUtils.hasText(user.getProfile())) {
			this.profile = user.getProfile();
		}
		if (StringUtils.hasText(user.getBlogTitle())) {
			this.blogTitle = user.getBlogTitle();
		}
		if (StringUtils.hasText(user.getGithubLink())) {
			this.githubLink = user.getGithubLink();
		}
		if (StringUtils.hasText(user.getInstagramLink())) {
			this.instagramLink = user.getInstagramLink();
		}
		if (StringUtils.hasText(user.getIntroduction())) {
			this.introduction = user.getIntroduction();
		}
	}
}
