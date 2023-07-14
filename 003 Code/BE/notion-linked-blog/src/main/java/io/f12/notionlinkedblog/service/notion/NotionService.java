package io.f12.notionlinkedblog.service.notion;

import java.util.List;

import org.springframework.stereotype.Service;

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

	private final NotionDevComponent notionDevComponent;

	public String test() {
		return retrieveBlocksFromPage("69150aac54af4cf7a9f3a5924ca5490e");
		// return retrieveBlocksFromPage("Test-Fin-696290e4edac4c77b0917df853bc309c");
	}

	public void saveNotionPage() {

	}

	private void retrievePage(String fullPath) {
		Block block;
		String blockId = convertPathToId(fullPath);
		NotionClient client = createClientInDev();
		RetrieveBlockRequest retrieveBlockRequest = new RetrieveBlockRequest(blockId);

		try {
			block = client.retrieveBlock(retrieveBlockRequest);
		} finally {
			client.close();
		}

	}

	private String retrieveBlocksFromPage(String fullPath) {
		Blocks blocks;
		String blockId = convertPathToId(fullPath);
		NotionClient client = createClientInDev();
		RetrieveBlockChildrenRequest retrieveBlockChildrenRequest
			= new RetrieveBlockChildrenRequest(blockId);
		try {
			blocks = client.retrieveBlockChildren(retrieveBlockChildrenRequest);
		} finally {
			client.close();
		}

		List<Block> blockedContents = blocks.getResults();

		NotionBlockConverter notionBlockConverter = new NotionBlockConverter();
		notionBlockConverter.setNotionClient(client);
		for (Block blockedContent : blockedContents) {
			notionBlockConverter.setType(blockedContent);
			notionBlockConverter.doFilter();
		}
		return notionBlockConverter.toString();
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
