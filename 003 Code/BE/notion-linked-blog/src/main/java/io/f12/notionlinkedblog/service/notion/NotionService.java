package io.f12.notionlinkedblog.service.notion;

import static io.f12.notionlinkedblog.exceptions.message.ExceptionMessages.NotionValidateMessages.*;
import static io.f12.notionlinkedblog.exceptions.message.ExceptionMessages.UserExceptionsMessages.*;
import static io.f12.notionlinkedblog.exceptions.message.ExceptionMessages.UserValidateMessages.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import io.f12.notionlinkedblog.component.oauth.NotionOAuthComponent;
import io.f12.notionlinkedblog.domain.notion.Notion;
import io.f12.notionlinkedblog.domain.notion.SyncedPages;
import io.f12.notionlinkedblog.domain.post.Post;
import io.f12.notionlinkedblog.domain.post.dto.PostSearchDto;
import io.f12.notionlinkedblog.domain.user.User;
import io.f12.notionlinkedblog.exceptions.exception.AlreadyExistException;
import io.f12.notionlinkedblog.exceptions.exception.NotionAuthenticationException;
import io.f12.notionlinkedblog.repository.notion.NotionDataRepository;
import io.f12.notionlinkedblog.repository.post.PostDataRepository;
import io.f12.notionlinkedblog.repository.syncedpages.SyncedPagesDataRepository;
import io.f12.notionlinkedblog.repository.user.UserDataRepository;
import io.f12.notionlinkedblog.service.notion.converter.contents.NotionBlockConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notion.api.v1.NotionClient;
import notion.api.v1.model.blocks.Block;
import notion.api.v1.model.blocks.Blocks;
import notion.api.v1.request.blocks.RetrieveBlockChildrenRequest;
import notion.api.v1.request.blocks.RetrieveBlockRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotionService {

	private final NotionOAuthComponent notionOAuthComponent;
	private final NotionDataRepository notionDataRepository;
	private final PostDataRepository postDataRepository;
	private final UserDataRepository userDataRepository;
	private final NotionBlockConverter notionBlockConverter;
	private final SyncedPagesDataRepository syncedPagesDataRepository;

	public void setNotionLinkPages(String path, Long userId) {
		User user = userDataRepository.findUserById(userId)
			.orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));

		String pathToId = convertPathToId(path);
		SyncedPages syncedPages = SyncedPages.builder()
			.pageId(pathToId)
			.user(user)
			.build();
		syncedPagesDataRepository.save(syncedPages);
	}

	//아래 도메인 으로 묶기
	public PostSearchDto saveNotionPageToBlogDev(String path, Long userId) throws NotionAuthenticationException {
		String convertPath = convertPathToId(path);
		Notion notion = notionDataRepository.findByPathValue(convertPath).orElse(null);
		if (notion != null) {
			throw new AlreadyExistException(DATA_ALREADY_EXIST);
		}
		String title = getTitle(convertPath, userId);
		String content = getContent(convertPath, userId);

		User user = userDataRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
		Post savePost = postDataRepository.save(Post.builder()
			.user(user)
			.title(title)
			.content(content)
			.viewCount(0L)
			.popularity(0.0)
			.isPublic(true)
			.build());
		notionDataRepository.save(Notion.builder()
			.notionId(convertPath)
			.post(savePost)
			.build());

		return PostSearchDto.builder()
			.postId(savePost.getId())
			.title(savePost.getTitle())
			.content(savePost.getContent())
			.viewCount(savePost.getViewCount())
			.likes(0)
			.requestThumbnailLink(savePost.getStoredThumbnailPath())
			.description(savePost.getDescription())
			.createdAt(LocalDateTime.now())
			.countOfComments(0)
			.author(user.getUsername())
			.isLiked(false)
			.build();
	}

	public PostSearchDto editNotionPageToBlogDev(Long userId, Long postId) throws NotionAuthenticationException {
		Post post = postDataRepository.findWithNotion(postId);
		Long postUserId = post.getUser().getId();
		checkSameUser(postUserId, userId);

		String content = getContent(post.getNotion().getNotionId(), userId);
		String title = getTitle(post.getNotion().getNotionId(), userId);
		post.editPost(title, content);

		return PostSearchDto.builder()
			.postId(post.getId())
			.title(post.getTitle())
			.content(post.getContent())
			.viewCount(post.getViewCount())
			.likes(post.getLikes().size())
			.requestThumbnailLink(post.getStoredThumbnailPath())
			.description(post.getDescription())
			.createdAt(post.getCreatedAt())
			.countOfComments(post.getComments().size())
			.author(post.getUser().getUsername())
			.isLiked(false)
			.build();
	}

	// private 매소드
	private String getTitle(String fullPath, Long userId) throws NotionAuthenticationException {
		Block block;
		NotionClient client = createClient(userId);
		RetrieveBlockRequest retrieveBlockRequest = new RetrieveBlockRequest(fullPath);
		try (client) {
			block = client.retrieveBlock(retrieveBlockRequest);
		}

		return block.asChildPage().getChildPage().getTitle();
	}

	private String getContent(String fullPath, Long userId) throws NotionAuthenticationException {
		Blocks blocks;
		NotionClient client = createClient(userId);
		StringBuilder stringBuilder = new StringBuilder();
		RetrieveBlockChildrenRequest retrieveBlockChildrenRequest
			= new RetrieveBlockChildrenRequest(fullPath);
		try {
			blocks = client.retrieveBlockChildren(retrieveBlockChildrenRequest);
		} finally {
			client.close();
		}

		List<Block> blockedContents = blocks.getResults();

		for (Block blockedContent : blockedContents) {
			stringBuilder.append(notionBlockConverter.doFilter(blockedContent, client));
		}
		return stringBuilder.toString();
	}

	private NotionClient createClient(Long userId) throws NotionAuthenticationException {
		String accessToken = userDataRepository.findUserByIdForNotionAuthToken(userId)
			.orElseThrow(NotionAuthenticationException::new)
			.getNotionOauth()
			.getAccessToken();

		NotionClient notionClient = new NotionClient();
		notionClient.setClientId(notionOAuthComponent.getClientId());
		notionClient.setClientSecret(notionOAuthComponent.getClientSecret());
		notionClient.setRedirectUri(notionOAuthComponent.getRedirectUrl());
		notionClient.setToken(accessToken);

		return notionClient;
	}

	private String convertPathToId(String path) {
		String[] split = path.split("-");
		return split[split.length - 1];
	}

	private void checkSameUser(Long id1, Long id2) {
		if (!id1.equals(id2)) {
			throw new AccessDeniedException(USER_NOT_MATCH);
		}
	}

}


