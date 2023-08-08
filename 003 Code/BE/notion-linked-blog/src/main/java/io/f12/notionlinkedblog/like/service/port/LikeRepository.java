package io.f12.notionlinkedblog.like.service.port;

import java.util.Optional;

import io.f12.notionlinkedblog.domain.likes.LikeEntity;
import io.f12.notionlinkedblog.domain.likes.dto.LikeSearchDto;

public interface LikeRepository {

	Optional<LikeSearchDto> findByUserIdAndPostId(Long userId, Long postId);

	void removeById(Long likeId);

	LikeEntity save(LikeEntity like);
}
