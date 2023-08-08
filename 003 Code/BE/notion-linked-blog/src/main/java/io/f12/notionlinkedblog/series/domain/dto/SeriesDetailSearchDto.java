package io.f12.notionlinkedblog.series.domain.dto;

import java.util.List;

import io.f12.notionlinkedblog.entity.common.PagingInfo;
import io.f12.notionlinkedblog.post.domain.dto.PostForDetailSeries;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SeriesDetailSearchDto {
	private Long seriesId;
	private String seriesName;
	private PagingInfo pagingInfo;
	private List<PostForDetailSeries> postsInfo;

}
