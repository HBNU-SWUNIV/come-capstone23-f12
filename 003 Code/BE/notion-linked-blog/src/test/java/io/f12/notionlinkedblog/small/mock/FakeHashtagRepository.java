package io.f12.notionlinkedblog.small.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import io.f12.notionlinkedblog.hashtag.infrastructure.HashtagEntity;
import io.f12.notionlinkedblog.hashtag.serivce.port.HashtagRepository;

public class FakeHashtagRepository implements HashtagRepository {

	private final AtomicLong autogeneratedId = new AtomicLong(0);
	private final List<HashtagEntity> data = Collections.synchronizedList(new ArrayList<>());

	@Override
	public HashtagEntity save(HashtagEntity hashtag) {
		if (hashtag.getId() == null || hashtag.getId() == 0) {
			hashtag.setId(autogeneratedId.incrementAndGet());
			data.add(hashtag);
		} else {
			data.removeIf(item -> Objects.equals(item.getId(), hashtag.getId()));
			data.add(hashtag);
		}
		return hashtag;
	}

	@Override
	public void delete(HashtagEntity hashtag) {
		data.removeIf(item -> Objects.equals(item.getId(), hashtag.getId()));
	}

	@Override
	public void deleteAll() {
		data.clear();
	}

	@Override
	public Optional<HashtagEntity> findByName(String name) {
		return data.stream().filter(hashtag -> hashtag.getName().equals(name)).findAny();
	}
}