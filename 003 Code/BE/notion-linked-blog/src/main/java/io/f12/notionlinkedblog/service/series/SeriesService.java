package io.f12.notionlinkedblog.service.series;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.f12.notionlinkedblog.domain.post.Post;
import io.f12.notionlinkedblog.domain.post.dto.SimplePostDto;
import io.f12.notionlinkedblog.domain.series.Series;
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

	// public PostSearchResponseDto getPostsBySeriesIdOrderByDesc(Long seriesId, Integer page) {
	// 	PageRequest pageRequest = PageRequest.of(page, pagingSize);
	// 	List<PostSearchDto> posts
	// 		= seriesDataRepository.findPostDtosBySeriesIdOrderByCreatedAtDesc(seriesId, pageRequest);
	// 	return buildPostSearchResponseDto(pageRequest, posts, pagingSize);
	// }
	//
	// public PostSearchResponseDto getPostsBySeriesIdOrderByAsc(Long seriesId, Integer page) {
	// 	PageRequest pageRequest = PageRequest.of(page, pagingSize);
	// 	List<PostSearchDto> posts
	// 		= seriesDataRepository.findPostDtosBySeriesIdOrderByCreatedAtAsc(seriesId, pageRequest);
	// 	return buildPostSearchResponseDto(pageRequest, posts, pagingSize);
	// }

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

	// public SeriesDetailSearchDto getDetailSeriesInfoOrderByDesc(Long seriesId, Integer page) {
	// 	PageRequest pageRequest = PageRequest.of(page, pagingSize);
	// 	List<Long> postIds = postDataRepository.findIdsBySeriesIdDesc(seriesId, pageRequest);
	// 	List<Post> posts = postDataRepository.findByIdsJoinWithSeriesOrderByCreatedAtDesc(postIds);
	// 	if (posts.size() == 0) {
	// 		Series series = seriesDataRepository.findById(seriesId)
	// 			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시리즈입니다"));
	// 		PagingInfo pagingInfo = PagingInfo.builder()
	// 			.pageSize(pagingSize)
	// 			.pageNow(page)
	// 			.elementSize(0)
	// 			.build();
	// 		return SeriesDetailSearchDto.builder()
	// 			.seriesName(series.getTitle())
	// 			.seriesId(series.getId())
	// 			.pagingInfo(pagingInfo)
	// 			.build();
	//
	// 	}
	//
	// 	List<PostForDetailSeries> postsDto = buildPostsForDetailSeries(posts);
	//
	// 	PagingInfo pagingInfo = PagingInfo.builder()
	// 		.pageSize(pagingSize)
	// 		.pageNow(page)
	// 		.elementSize(posts.size())
	// 		.build();
	//
	// 	return SeriesDetailSearchDto.builder()
	// 		.seriesId(posts.get(0).getSeries().getId())
	// 		.seriesName(posts.get(0).getSeries().getTitle())
	// 		.pagingInfo(pagingInfo)
	// 		.postsInfo(postsDto)
	// 		.build();
	// }
	//
	// public SeriesDetailSearchDto getDetailSeriesInfoOrderByAsc(Long seriesId, Integer page) {
	// 	PageRequest pageRequest = PageRequest.of(page, pagingSize);
	// 	List<Long> postIds = postDataRepository.findIdsBySeriesIdDesc(seriesId, pageRequest);
	// 	List<Post> posts = postDataRepository.findByIdsJoinWithSeriesOrderByCreatedAtAsc(postIds);
	// 	if (posts.size() == 0) {
	// 		Series series = seriesDataRepository.findById(seriesId)
	// 			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시리즈입니다"));
	// 		PagingInfo pagingInfo = PagingInfo.builder()
	// 			.pageSize(pagingSize)
	// 			.pageNow(page)
	// 			.elementSize(0)
	// 			.build();
	// 		return SeriesDetailSearchDto.builder()
	// 			.seriesName(series.getTitle())
	// 			.seriesId(series.getId())
	// 			.pagingInfo(pagingInfo)
	// 			.build();
	//
	// 	}
	//
	// 	List<PostForDetailSeries> postsDto = buildPostsForDetailSeries(posts);
	//
	// 	PagingInfo pagingInfo = PagingInfo.builder()
	// 		.pageSize(pagingSize)
	// 		.pageNow(page)
	// 		.elementSize(posts.size())
	// 		.build();
	//
	// 	return SeriesDetailSearchDto.builder()
	// 		.seriesId(posts.get(0).getSeries().getId())
	// 		.seriesName(posts.get(0).getSeries().getTitle())
	// 		.pagingInfo(pagingInfo)
	// 		.postsInfo(postsDto)
	// 		.build();
	// }
	//
	// // 내부 사용 매서드
	// private PostSearchResponseDto buildPostSearchResponseDto(PageRequest paging, List<PostSearchDto> dto, int size) {
	// 	return PostSearchResponseDto.builder()
	// 		.pageSize(paging.getPageSize())
	// 		.pageNow(paging.getPageNumber())
	// 		.posts(dto)
	// 		.elementsSize(size)
	// 		.build();
	// }
	//
	// private List<PostForDetailSeries> buildPostsForDetailSeries(List<Post> posts) {
	// 	return posts.stream().map(p -> {
	// 		return PostForDetailSeries.builder()
	// 			.postTitle(p.getTitle())
	// 			.postInfo(p.getDescription())
	// 			.thumbnailPath(p.getStoredThumbnailPath())
	// 			.build();
	// 	}).collect(Collectors.toList());
	// }

}
