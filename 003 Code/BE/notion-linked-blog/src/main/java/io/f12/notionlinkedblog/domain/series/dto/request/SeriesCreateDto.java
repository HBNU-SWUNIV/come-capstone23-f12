package io.f12.notionlinkedblog.domain.series.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SeriesCreateDto {
	Long userId;
	String seriesTitle;
}