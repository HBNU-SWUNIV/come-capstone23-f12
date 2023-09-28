package io.f12.notionlinkedblog.notion.infrastructure.multi;

import org.springframework.data.jpa.repository.JpaRepository;

import io.f12.notionlinkedblog.notion.service.port.SyncedSeriesRepository;

public interface SyncedSeriesDataRepository extends JpaRepository<SyncedSeriesEntity, Long>, SyncedSeriesRepository {

}
