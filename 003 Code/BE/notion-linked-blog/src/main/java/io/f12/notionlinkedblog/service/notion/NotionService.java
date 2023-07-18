package io.f12.notionlinkedblog.service.notion;

import java.util.List;

import org.springframework.stereotype.Service;

import io.f12.notionlinkedblog.repository.post.PostDataRepository;
import io.f12.notionlinkedblog.repository.user.UserDataRepository;
import io.f12.notionlinkedblog.service.notion.converter.contents.NotionBlockConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notion.api.v1.NotionClient;
import notion.api.v1.model.blocks.Block;
import notion.api.v1.model.blocks.Blocks;
import notion.api.v1.request.blocks.RetrieveBlockChildrenRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotionService {

	private final NotionDevComponent notionDevComponent;
	private final PostDataRepository postDataRepository;
	private final UserDataRepository userDataRepository;
	private final NotionBlockConverter notionBlockConverter;

	public String test() {
		return getContent("Test-Fin-696290e4edac4c77b0917df853bc309c");
	}

	// public void saveNotionPage(String path, Long userId) {
	// 	String title = getTitle(path);
	// 	String content = getContent(path);
	//
	// 	User user = userDataRepository.findById(userId)
	// 		.orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
	//
	// 	// Post.builder()
	// 	// 	.user(user)
	// 	// 	.title(title)
	// 	// 	.content(content)
	// 	// 	.
	//
	// }

	// private String getTitle(String fullPath) {
	// 	Block block;
	// 	String blockId = convertPathToId(fullPath);
	// 	NotionClient client = createClientInDev();
	// 	RetrieveBlockRequest retrieveBlockRequest = new RetrieveBlockRequest(blockId);
	//
	// 	try {
	// 		block = client.retrieveBlock(retrieveBlockRequest);
	// 	} finally {
	// 		client.close();
	// 	}
	//
	// 	return block.asChildPage().getChildPage().getTitle();
	// }

	private String getContent(String fullPath) {
		Blocks blocks;
		String blockId = convertPathToId(fullPath);
		NotionClient client = createClientInDev();
		StringBuilder stringBuilder = new StringBuilder();
		RetrieveBlockChildrenRequest retrieveBlockChildrenRequest
			= new RetrieveBlockChildrenRequest(blockId);
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

	private String convertPathToId(String path) {
		String[] split = path.split("-");
		return split[split.length - 1];
	}

	private NotionClient createClientInDev() {
		NotionClient client = new NotionClient();
		String internalSecret = notionDevComponent.getInternalSecret();
		client.setToken(internalSecret);
		return client;
	}
}
