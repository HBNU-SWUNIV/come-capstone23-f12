package io.f12.notionlinkedblog.service.notion.converter.contents.filter;

import java.util.List;

import io.f12.notionlinkedblog.service.notion.converter.contents.NotionBlockConverter;
import io.f12.notionlinkedblog.service.notion.converter.contents.type.NotionBlockType;
import notion.api.v1.NotionClient;
import notion.api.v1.model.blocks.Block;
import notion.api.v1.model.blocks.Blocks;
import notion.api.v1.request.blocks.RetrieveBlockChildrenRequest;

public class SyncedBlockFilter implements NotionFilter {
	@Override
	public boolean isAcceptable(Block block) {
		return block.getType().getValue().equals(NotionBlockType.Block.SYNCED_BLOCK);
	}

	@Override
	public String doFilter(Block block, NotionClient client) {
		String id = block.getId();
		return reRequestInnerData(id, client);
	}

	private String reRequestInnerData(String id, NotionClient client) {
		Blocks blocks;
		NotionBlockConverter notionBlockConverter = new NotionBlockConverter();
		StringBuilder stringBuilder = new StringBuilder();
		RetrieveBlockChildrenRequest retrieveBlockChildrenRequest
			= new RetrieveBlockChildrenRequest(id);
		try (client) {
			blocks = client.retrieveBlockChildren(retrieveBlockChildrenRequest);
		}
		List<Block> results = blocks.getResults();
		for (Block block : results) {
			stringBuilder.append(notionBlockConverter.doFilter(block, client));
		}

		return stringBuilder.toString();
	}
}
