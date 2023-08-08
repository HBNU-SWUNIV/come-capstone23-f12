package io.f12.notionlinkedblog.notion.service.port;

import java.util.List;

import io.f12.notionlinkedblog.domain.notion.SyncedPages;

public interface SyncedPagesRepository {
	List<SyncedPages> findAll();

	SyncedPages save(SyncedPages syncedPages);
}
