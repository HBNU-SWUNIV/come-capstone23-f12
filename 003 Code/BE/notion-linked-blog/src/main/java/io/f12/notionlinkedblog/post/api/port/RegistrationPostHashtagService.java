package io.f12.notionlinkedblog.post.api.port;

import java.util.List;

import io.f12.notionlinkedblog.post.domain.Post;

public interface RegistrationPostHashtagService {

	void addHashtags(List<String> hashtags, Post post);
}
