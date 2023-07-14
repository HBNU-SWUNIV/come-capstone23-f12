package io.f12.notionlinkedblog.service.notion.converter.contents.filter;

import java.util.List;

import io.f12.notionlinkedblog.service.notion.converter.contents.CheckAnnotations;
import io.f12.notionlinkedblog.service.notion.converter.contents.NotionBlockConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import notion.api.v1.NotionClient;
import notion.api.v1.model.blocks.Block;
import notion.api.v1.model.blocks.Blocks;
import notion.api.v1.model.pages.PageProperty;
import notion.api.v1.request.blocks.RetrieveBlockChildrenRequest;

@AllArgsConstructor
@Builder
public class ToggleBlockFilter {
	private Block block;
	private final NotionClient notionClient;

	public String doFilter() {
		// 		<details>
		//     <summary>토글 접기/펼치기</summary>
		//     <div>
		// 			test
		// 		</div>
		// </details>
		String id = block.asToggle().getId();
		List<PageProperty.RichText> texts = block.asToggle().getToggle().getRichText();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<details>\n").append("<summary>");
		for (PageProperty.RichText text : texts) {
			CheckAnnotations letterShape = new CheckAnnotations(text);
			stringBuilder.append(letterShape.applyAnnotations(text));
		}
		stringBuilder.append("</summary>\n").append("<div>\n");
		stringBuilder.append(internalFilter(id)).append("\n");
		stringBuilder.append("</div>\n").append("</details>");

		return stringBuilder + "\n";
	}

	private String internalFilter(String id) {
		List<Block> blocks = reRequestContents(id);
		NotionBlockConverter converter = new NotionBlockConverter();
		converter.setNotionClient(notionClient);
		for (Block block : blocks) {
			converter.setType(block);
			converter.doFilter();
		}
		return converter.toString();
	}

	private List<Block> reRequestContents(String id) {
		Blocks blocks;
		RetrieveBlockChildrenRequest retrieveBlockChildrenRequest
			= new RetrieveBlockChildrenRequest(id);
		try {
			blocks = notionClient.retrieveBlockChildren(retrieveBlockChildrenRequest);
		} finally {
			notionClient.close();
		}
		return blocks.getResults();
	}
}
