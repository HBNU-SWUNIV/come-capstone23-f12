package io.f12.notionlinkedblog.service.series;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import io.f12.notionlinkedblog.api.common.Endpoint;
import io.f12.notionlinkedblog.domain.common.PagingInfo;
import io.f12.notionlinkedblog.domain.post.Post;
import io.f12.notionlinkedblog.domain.post.dto.PostForDetailSeries;
import io.f12.notionlinkedblog.domain.post.dto.SimplePostDto;
import io.f12.notionlinkedblog.domain.series.Series;
import io.f12.notionlinkedblog.domain.series.dto.SeriesDetailSearchDto;
import io.f12.notionlinkedblog.domain.series.dto.SeriesSimpleSearchDto;
import io.f12.notionlinkedblog.repository.post.PostDataRepository;
import io.f12.notionlinkedblog.repository.series.SeriesDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeriesService {

	private final SeriesDataRepository seriesDataRepository;
	private final PostDataRepository postDataRepository;

	private static final int pagingSize = 10;

	public SeriesSimpleSearchDto getSimpleSeriesInfo(Long seriesId) {
		Series series = seriesDataRepository.findSeriesById(seriesId)
			.orElseThrow(() -> new IllegalArgumentException("잘못된 seriesId 입니다."));

		List<Post> post = series.getPost();
		List<SimplePostDto> simplePosts = postToSimplePost(post);

		return SeriesSimpleSearchDto.builder()
			.seriesName(series.getTitle())
			.seriesId(series.getId())
			.posts(simplePosts)
			.build();
	}

	private List<SimplePostDto> postToSimplePost(List<Post> post) {
		return post.stream().map(p -> SimplePostDto.builder()
			.postId(p.getId())
			.postTitle(p.getTitle())
			.build()).collect(Collectors.toList());
	}

	public SeriesDetailSearchDto getDetailSeriesInfoOrderByDesc(Long seriesId, Integer page) {
		PageRequest pageRequest = PageRequest.of(page, pagingSize);

		List<Long> postIds = postDataRepository.findIdsBySeriesIdDesc(seriesId, pageRequest);
		List<Post> posts = postDataRepository.findByIdsJoinWithSeries(postIds);
		Series series = posts.get(0).getSeries();

		List<PostForDetailSeries> postDtos = posts.stream().map(p -> {
			return PostForDetailSeries.builder()
				.postTitle(p.getTitle())
				.postInfo(p.getDescription())
				.thumbnailRequestUrl(thumbnailPathToRequestUrl(p.getThumbnailName()))
				.build();
		}).collect(Collectors.toList());

		PagingInfo pagingInfo = PagingInfo.builder()
			.pageNow(page)
			.elementSize(postDtos.size())
			.build();

		return SeriesDetailSearchDto.builder()
			.seriesId(series.getId())
			.seriesName(series.getTitle())
			.pagingInfo(pagingInfo)
			.postsInfo(postDtos)
			.build();
	}

	public SeriesDetailSearchDto getDetailSeriesInfoOrderByAsc(Long seriesId, Integer page) {
		PageRequest pageRequest = PageRequest.of(page, pagingSize);

		List<Long> postIds = postDataRepository.findIdsBySeriesIdAsc(seriesId, pageRequest);
		List<Post> posts = postDataRepository.findByIdsJoinWithSeries(postIds);
		Series series = posts.get(0).getSeries();

		List<PostForDetailSeries> postDtos = posts.stream().map(p -> {
			return PostForDetailSeries.builder()
				.postTitle(p.getTitle())
				.postInfo(p.getDescription())
				.thumbnailRequestUrl(thumbnailPathToRequestUrl(p.getThumbnailName()))
				.build();
		}).collect(Collectors.toList());

		PagingInfo pagingInfo = PagingInfo.builder()
			.pageNow(page)
			.elementSize(postDtos.size())
			.build();

		return SeriesDetailSearchDto.builder()
			.seriesId(series.getId())
			.seriesName(series.getTitle())
			.pagingInfo(pagingInfo)
			.postsInfo(postDtos)
			.build();

	}

	// 내부 사용 매서드
	private String thumbnailPathToRequestUrl(String thumbnailName) {
		return Endpoint.Api.REQUEST_THUMBNAIL_IMAGE + thumbnailName;
	}

}
