package io.f12.notionlinkedblog.repository.notion;

import org.springframework.data.jpa.repository.JpaRepository;

import io.f12.notionlinkedblog.domain.notion.Notion;

public interface NotionDataRepository extends JpaRepository<Notion, Long> {
}
