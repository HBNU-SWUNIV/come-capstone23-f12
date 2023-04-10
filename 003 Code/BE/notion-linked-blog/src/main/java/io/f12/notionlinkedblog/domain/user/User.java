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

	public void editProfile(String username, String email, String password, String profile, String blogTitle,
		String githubLink, String instagramLink, String introduction) {

		if (StringUtils.hasText(username)) {
			this.username = username;
		}
		if (StringUtils.hasText(email)) {
			this.email = email;
		}
		if (StringUtils.hasText(password)) {
			this.password = password;
		}
		if (StringUtils.hasText(profile)) {
			this.profile = profile;
		}
		if (StringUtils.hasText(blogTitle)) {
			this.blogTitle = blogTitle;
		}
		if (StringUtils.hasText(githubLink)) {
			this.githubLink = githubLink;
		}
		if (StringUtils.hasText(instagramLink)) {
			this.instagramLink = instagramLink;
		}
		if (StringUtils.hasText(introduction)) {
			this.introduction = introduction;
		}
	}
}
