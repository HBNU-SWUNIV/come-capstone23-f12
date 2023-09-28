package io.f12.notionlinkedblog.notion.infrastructure.multi;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.f12.notionlinkedblog.notion.service.port.SyncedSeriesRepository;

public interface SyncedSeriesDataRepository extends JpaRepository<SyncedSeriesEntity, Long>, SyncedSeriesRepository {

	@Query("SELECT s FROM SyncedSeriesEntity s WHERE s.pageId =:id")
	Optional<SyncedSeriesEntity> findByPageId(@Param("id") String id);
}
