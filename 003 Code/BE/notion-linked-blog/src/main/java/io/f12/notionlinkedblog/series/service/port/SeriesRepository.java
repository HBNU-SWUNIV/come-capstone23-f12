package io.f12.notionlinkedblog.series.service.port;

import java.util.Optional;

import io.f12.notionlinkedblog.domain.series.Series;

public interface SeriesRepository {
	Optional<Series> findSeriesById(Long seriesId);

	Series save(Series series);

	void delete(Series series);

	void deleteAll();
}
