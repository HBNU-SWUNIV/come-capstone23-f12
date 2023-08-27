package io.f12.notionlinkedblog.hashtag.serivce;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.f12.notionlinkedblog.hashtag.domain.Hashtag;
import io.f12.notionlinkedblog.hashtag.infrastructure.HashtagEntity;
import io.f12.notionlinkedblog.hashtag.serivce.port.HashtagRepository;
import io.f12.notionlinkedblog.hashtag.serivce.port.PostHashtagRepository;
import io.f12.notionlinkedblog.post.domain.Post;
import io.f12.notionlinkedblog.post.service.port.PostRepository;
import io.f12.notionlinkedblog.post.service.port.RegistrationPostHashtagService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements RegistrationPostHashtagService {

	private final HashtagRepository hashtagRepository;
	private final PostHashtagRepository postHashtagRepository;
	private final PostRepository postRepository;

	@Override
	public void addHashtags(List<String> hashtags, Post post) {
		List<Hashtag> domainHashtag = findHashtag(hashtags);

		addPostsToHashtag(post, domainHashtag);
		post.changeHashtags(domainHashtag);

		postRepository.save(post.toEntity());
		for (Hashtag hashtag : domainHashtag) {
			hashtagRepository.save(hashtag.toEntity());
		}
	}

	private void addPostsToHashtag(Post post, List<Hashtag> domainHashtag) {
		for (Hashtag hashtag : domainHashtag) {
			hashtag.addPost(post);
		}
	}

	private List<Hashtag> findHashtag(List<String> hashtags) {
		List<Hashtag> returnHashtags = new ArrayList<>();
		for (String hashtag : hashtags) {
			Optional<HashtagEntity> findEntity = hashtagRepository.findByName(hashtag);
			if (findEntity.isPresent()) {
				returnHashtags.add(findEntity.get().toModel());
			} else {
				returnHashtags.add(Hashtag.builder()
					.name(hashtag)
					.post(new ArrayList<>())
					.build());
			}
		}

		return returnHashtags;
	}
}
