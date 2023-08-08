package io.f12.notionlinkedblog.post.infrastructure;

import static io.f12.notionlinkedblog.domain.likes.QLike.*;
import static io.f12.notionlinkedblog.domain.post.QPost.*;
import static io.f12.notionlinkedblog.domain.series.QSeries.*;
import static io.f12.notionlinkedblog.domain.user.QUser.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import io.f12.notionlinkedblog.domain.post.Post;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostDataRepositoryImpl implements PostRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<Long> findPostIdsByTitle(String title, Pageable pageable) {
		return queryFactory.select(post.id)
			.from(post)
			.where(post.title.contains(title).and(post.isPublic.isTrue()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	@Override
	public List<Long> findPostIdsByContent(String content, Pageable pageable) {
		return queryFactory.select(post.id)
			.from(post)
			.where(post.content.contains(content).and(post.isPublic.isTrue()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	@Override
	public List<Long> findLatestPostIdsByCreatedAtDesc(Pageable pageable) {
		return queryFactory.select(post.id)
			.from(post)
			.where(post.isPublic.isTrue())
			.orderBy(post.createdAt.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	@Override
	public List<Long> findPopularityPostIdsByViewCountAtDesc(Pageable pageable) {
		return queryFactory.select(post.id)
			.from(post)
			.where(post.isPublic.isTrue())
			.orderBy(post.popularity.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	@Override
	public List<Post> findByPostIdsJoinWithUserAndLikeOrderByLatest(List<Long> ids) {
		return queryFactory.selectFrom(post)
			.leftJoin(post.user, user)
			.fetchJoin()
			.leftJoin(post.likes, like)
			.fetchJoin()
			.where(post.id.in(ids))
			.orderBy(post.createdAt.asc())
			.distinct()
			.fetch();
	}

	@Override
	public List<Post> findByPostIdsJoinWithUserAndLikeOrderByTrend(List<Long> ids) {
		return queryFactory.selectFrom(post)
			.leftJoin(post.user, user)
			.fetchJoin()
			.leftJoin(post.likes, like)
			.fetchJoin()
			.where(post.id.in(ids))
			.orderBy(post.popularity.desc())
			.distinct()
			.fetch();
	}

	@Override
	public List<Long> findIdsBySeriesIdDesc(Long seriesId, Pageable pageable) {
		return queryFactory
			.select(post.id)
			.from(post)
			.where(post.series.id.eq(seriesId).and(post.isPublic.isTrue()))
			.orderBy(post.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	@Override
	public List<Long> findIdsBySeriesIdAsc(Long seriesId, Pageable pageable) {
		return queryFactory
			.select(post.id)
			.from(post)
			.where(post.series.id.eq(seriesId).and(post.isPublic.isTrue()))
			.orderBy(post.createdAt.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	@Override
	public List<Post> findByIdsJoinWithSeries(List<Long> ids) {
		return queryFactory.selectFrom(post)
			.leftJoin(post.series, series)
			.fetchJoin()
			.where(post.id.in(ids))
			.distinct()
			.fetch();

	}

}
