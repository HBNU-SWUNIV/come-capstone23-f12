package io.f12.notionlinkedblog.oauth.common.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import io.f12.notionlinkedblog.oauth.github.service.port.GithubOauthDataRepository;

public interface CommonOAuthDataRepository extends JpaRepository<CommonOAuthEntity, Long>, GithubOauthDataRepository {

}
