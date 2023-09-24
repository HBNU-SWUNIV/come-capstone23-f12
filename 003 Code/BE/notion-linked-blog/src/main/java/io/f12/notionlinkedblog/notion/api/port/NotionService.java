package io.f12.notionlinkedblog.notion.api.port;

import java.time.LocalDateTime;
import java.util.List;

import io.f12.notionlinkedblog.common.exceptions.exception.NotionAuthenticationException;
import io.f12.notionlinkedblog.post.api.response.PostSearchDto;
import io.f12.notionlinkedblog.post.infrastructure.PostEntity;

public interface NotionService {

	public PostSearchDto saveSingleNotionPage(String path, Long userId) throws NotionAuthenticationException;

	void editNotionPageToBlog(Long userId, PostEntity post) throws NotionAuthenticationException;

	void initEveryPages(List<String> pageIds, Long userId, String accessCode)
		throws NotionAuthenticationException;

	boolean needUpdate(Long userId, String pageId, LocalDateTime updateTime)
		throws NotionAuthenticationException;
}
