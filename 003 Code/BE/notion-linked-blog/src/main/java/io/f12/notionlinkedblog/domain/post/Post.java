package io.f12.notionlinkedblog.domain.post;

import static javax.persistence.FetchType.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.util.StringUtils;

import io.f12.notionlinkedblog.domain.PostTimeEntity;
import io.f12.notionlinkedblog.domain.series.Series;
import io.f12.notionlinkedblog.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "posts")
public class Post extends PostTimeEntity {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	@NotNull
	private User user;
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "series_id")
	private Series seriesId;
	private String title;
	private String content;
	private String thumbnail;
	private Long viewCount;

	@Builder
	public Post(User user, Series seriesId, String title, String content, String thumbnail, Long viewCount) {
		this.user = user;
		this.seriesId = seriesId;
		this.title = title;
		this.content = content;
		this.thumbnail = thumbnail;
		this.viewCount = viewCount;
	}

	public void editPost(String title, String content, String thumbnail) {
		if (StringUtils.hasText(title)) {
			this.title = title;
		}
		if (StringUtils.hasText(content)) {
			this.content = content;
		}
		if (StringUtils.hasText(thumbnail)) {
			this.thumbnail = thumbnail;
		}
	}

}
