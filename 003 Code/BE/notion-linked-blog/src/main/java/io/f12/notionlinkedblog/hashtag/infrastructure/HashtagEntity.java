package io.f12.notionlinkedblog.hashtag.infrastructure;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import io.f12.notionlinkedblog.hashtag.domain.Hashtag;
import io.f12.notionlinkedblog.post.infrastructure.PostEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
@Table(name = "hashtags")
public class HashtagEntity {
	@Id
	private Long id;
	@NotNull
	@Column(unique = true)
	private String name;

	@ManyToMany
	@JoinTable(name = "posts_hashtags",
		joinColumns = @JoinColumn(name = "hashtags_id"),
		inverseJoinColumns = @JoinColumn(name = "post_id"))
	private List<PostEntity> post;

	public Hashtag toModel() {
		return Hashtag.builder()
			.id(this.id)
			.name(this.name)
			.post(this.post.stream().map(PostEntity::toModel).collect(Collectors.toList()))
			.build();
	}

}
