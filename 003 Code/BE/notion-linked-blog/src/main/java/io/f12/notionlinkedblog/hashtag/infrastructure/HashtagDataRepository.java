package io.f12.notionlinkedblog.hashtag.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import io.f12.notionlinkedblog.hashtag.serivce.port.HashtagRepository;

public interface HashtagDataRepository extends JpaRepository<Long, HashtagEntity>, HashtagRepository {
}
