package io.f12.notionlinkedblog.service.notion;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.f12.notionlinkedblog.service.notion.blockconverter.NotionBlockConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notion.api.v1.NotionClient;
import notion.api.v1.model.blocks.Block;
import notion.api.v1.model.blocks.Blocks;
import notion.api.v1.model.blocks.ParagraphBlock;
import notion.api.v1.model.pages.PageProperty;
import notion.api.v1.request.blocks.RetrieveBlockChildrenRequest;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotionService {

	public String test() {
		Blocks blocks;
		NotionClient client = new NotionClient();
		client.setToken("secret_AzYyixQsvIQCSvgcVhuCvWoNk17xYLlwuREu6iklR2S");
		// client.setClientId("test_api");
		// client.setClientSecret("secret_AzYyixQsvIQCSvgcVhuCvWoNk17xYLlwuREu6iklR2S");
		RetrieveBlockChildrenRequest retrieveBlockChildrenRequest =
			new RetrieveBlockChildrenRequest("696290e4edac4c77b0917df853bc309c");
		try {
			blocks = client.retrieveBlockChildren(retrieveBlockChildrenRequest);
		} finally {
			client.close();
		}

		List<Block> results = blocks.getResults();
		Block block = results.get(0);
		ParagraphBlock paragraph = block.asParagraph();
		List<PageProperty.RichText> texts = paragraph.getParagraph().getRichText();

		return blocks.toString();
	}

	public String test2() {
		return retrieveBlocksFromPage("Test-Fin-696290e4edac4c77b0917df853bc309c");
	}

	public String retrieveBlocksFromPage(String fullPath) {
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

		for (Block blockedContent : blockedContents) {
			notionBlockConverter.setType(blockedContent);
			notionBlockConverter.doFilter();
		}
		return notionBlockConverter.toString();
	}

	public List<Block> reRequestTable(String id, int width) {
		Blocks blocks;
		NotionClient client = createClientInDev();
		RetrieveBlockChildrenRequest retrieveBlockChildrenRequest
			= new RetrieveBlockChildrenRequest(id);
		try {
			blocks = client.retrieveBlockChildren(retrieveBlockChildrenRequest);
		} finally {
			client.close();
		}
		return blocks.getResults();
	}

	private String convertPathToId(String path) {
		//Test-Start-476c1d6eaeaf49fd8bc4fc4798b05dee
		String[] split = path.split("-");
		return split[split.length - 1];
	}

	private NotionClient createClientInDev() {
		NotionClient client = new NotionClient();
		client.setToken("secret_AzYyixQsvIQCSvgcVhuCvWoNk17xYLlwuREu6iklR2S");
		return client;
	}
}
