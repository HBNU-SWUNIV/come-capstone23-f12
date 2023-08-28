package io.f12.notionlinkedblog.post.service.port;

import java.util.List;

import io.f12.notionlinkedblog.post.infrastructure.PostEntity;

public interface RegistrationPostHashtagService {

	void addHashtags(List<String> hashtags, PostEntity post);

	void editHashtags(List<String> hashtagList, PostEntity post);
}
