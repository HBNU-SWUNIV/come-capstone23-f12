package io.f12.notionlinkedblog.oauth.common.infrastructure;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.f12.notionlinkedblog.oauth.common.domain.OAuthType;
import io.f12.notionlinkedblog.user.infrastructure.UserEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "common_oauth")
public class CommonOAuthEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	private String accessToken;
	@Column(unique = true)
	private String refreshToken;

	@Enumerated(EnumType.STRING)
	private OAuthType type;

	@ManyToOne(fetch = FetchType.LAZY)
	private UserEntity user;

}
