package io.f12.notionlinkedblog.service.notion.converter.contents.filter;

import java.util.List;

import io.f12.notionlinkedblog.service.notion.converter.contents.CheckAnnotations;
import io.f12.notionlinkedblog.service.notion.converter.contents.NotionBlockConverter;
import io.f12.notionlinkedblog.service.notion.converter.contents.type.NotionBlockType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import notion.api.v1.NotionClient;
import notion.api.v1.model.blocks.Block;
import notion.api.v1.model.blocks.BulletedListItemBlock;
import notion.api.v1.model.pages.PageProperty;
import notion.api.v1.request.blocks.RetrieveBlockChildrenRequest;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BulletedListItemFilter implements NotionFilter {
	private Integer deep;

	@Override
	public boolean isAcceptable(Block block) {
		return block.getType().getValue().equals(NotionBlockType.Block.BULLETED_LIST_ITEM);
	}

	@Override
	public String doFilter(Block block, NotionClient client) {
		BulletedListItemBlock bulletedListItem = block.asBulletedListItem();
		List<PageProperty.RichText> texts = block.asBulletedListItem().getBulletedListItem().getRichText();
		StringBuilder stringBuilder = new StringBuilder();

		for (PageProperty.RichText text : texts) {
			CheckAnnotations letterShape = new CheckAnnotations(text);
			stringBuilder.append(letterShape.applyAnnotations(text));
		}
		stringBuilder.append("  ").append("\n");
		if (bulletedListItem.getHasChildren()) {
			if (deep == null) {
				stringBuilder.append(getChildren(bulletedListItem.getId(), client, 1));
			} else {
				stringBuilder.append(getChildren(bulletedListItem.getId(), client, deep + 1));
			}
		}
		// stringBuilder.append("\n");
		return "- " + stringBuilder;
	}

	private String getChildren(String id, NotionClient client, Integer deep) {
		RetrieveBlockChildrenRequest retrieveBlockChildrenRequest = new RetrieveBlockChildrenRequest(id);
		List<Block> results = null;
		try (client) {
			results = client.retrieveBlockChildren(retrieveBlockChildrenRequest).getResults();
		}
		NotionBlockConverter notionBlockConverter = new NotionBlockConverter(deep);
		StringBuilder stringBuilder = new StringBuilder();

		for (Block block : results) {
			for (int i = 0; i < deep; i++) {
				stringBuilder.append("\t");
			}
			stringBuilder.append(notionBlockConverter.doFilter(block, client)).append("\n");
		}

		return stringBuilder.toString();
	}
}
