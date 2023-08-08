package io.f12.notionlinkedblog.series.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserSeriesDto {
	Long seriesId;
	String seriesName;
}
