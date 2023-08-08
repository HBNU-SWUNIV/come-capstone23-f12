package io.f12.notionlinkedblog.notion.service.port;

import java.util.List;

import io.f12.notionlinkedblog.entity.notion.SyncedPagesEntity;

public interface SyncedPagesRepository {
	List<SyncedPagesEntity> findAll();

	SyncedPagesEntity save(SyncedPagesEntity syncedPages);
}
