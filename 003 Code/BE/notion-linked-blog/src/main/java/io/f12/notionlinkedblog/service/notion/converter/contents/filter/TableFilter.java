package io.f12.notionlinkedblog.service.notion.converter.contents.filter;

import java.util.List;

import io.f12.notionlinkedblog.service.notion.converter.contents.CheckAnnotations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import notion.api.v1.NotionClient;
import notion.api.v1.model.blocks.Block;
import notion.api.v1.model.blocks.Blocks;
import notion.api.v1.model.pages.PageProperty;
import notion.api.v1.request.blocks.RetrieveBlockChildrenRequest;

@AllArgsConstructor
@Builder
public class TableFilter {
	private Block block;
	private final NotionClient notionClient;

	public String doFilter() {
		int tableWidth = block.asTable().getTable().getTableWidth();
		String id = block.getId();
		return internalFunction(id, tableWidth) + "\n";
	}

	private String internalFunction(String id, int width) {
		List<Block> results = reRequestTable(id, width, notionClient);

		int row = results.get(0).asTableRow().getTableRow().getCells().size();
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < width + 1; i++) {
			stringBuilder.append("|");
			if (i == 1) {
				for (int j = 0; j < row; j++) {
					stringBuilder.append("--|");
				}
				stringBuilder.append("\n|");
			}
			Block selectBlock = results.get(i);
			List<List<PageProperty.RichText>> cells = selectBlock.asTableRow().getTableRow().getCells();
			for (List<PageProperty.RichText> cell : cells) {
				for (PageProperty.RichText richText : cell) {
					CheckAnnotations letterShape = new CheckAnnotations(richText);
					stringBuilder.append(letterShape.applyAnnotations(richText));
				}
				stringBuilder.append("|");
			}
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}

	private List<Block> reRequestTable(String id, int width, NotionClient client) {
		Blocks blocks;
		RetrieveBlockChildrenRequest retrieveBlockChildrenRequest
			= new RetrieveBlockChildrenRequest(id);
		try {
			blocks = client.retrieveBlockChildren(retrieveBlockChildrenRequest);
		} finally {
			client.close();
		}
		return blocks.getResults();
	}

}
