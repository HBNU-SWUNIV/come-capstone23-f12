package io.f12.notionlinkedblog.repository.syncedpages;

import org.springframework.data.jpa.repository.JpaRepository;

import io.f12.notionlinkedblog.domain.notion.SyncedPages;

public interface SyncedPagesDataRepository extends JpaRepository<SyncedPages, Long> {
}
