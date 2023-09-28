package io.f12.notionlinkedblog.notion.service.port;

import java.util.Optional;

import io.f12.notionlinkedblog.notion.infrastructure.multi.SyncedSeriesEntity;

public interface SyncedSeriesRepository {
	SyncedSeriesEntity save(SyncedSeriesEntity entity);

	Optional<SyncedSeriesEntity> findByPageId(String id);
}
