package io.f12.notionlinkedblog.service.notion.converter.contents.filter;

import java.util.List;

import io.f12.notionlinkedblog.service.notion.converter.contents.CheckAnnotations;
import io.f12.notionlinkedblog.service.notion.converter.contents.NotionBlockConverter;
import io.f12.notionlinkedblog.service.notion.converter.contents.type.NotionBlockType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import notion.api.v1.NotionClient;
import notion.api.v1.model.blocks.Block;
import notion.api.v1.model.blocks.ParagraphBlock;
import notion.api.v1.model.pages.PageProperty;
import notion.api.v1.request.blocks.RetrieveBlockChildrenRequest;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ParagraphFilter implements NotionFilter {

	private Integer deep;

	@Override
	public boolean isAcceptable(Block block) {
		return block.getType().getValue().equals(NotionBlockType.Block.PARAGRAPH);
	}

	@Override
	public String doFilter(Block block, NotionClient client) {
		ParagraphBlock paragraph = block.asParagraph();
		List<PageProperty.RichText> texts = paragraph.getParagraph().getRichText();
		StringBuilder stringBuilder = new StringBuilder();

		for (PageProperty.RichText text : texts) {
			CheckAnnotations letterShape = new CheckAnnotations(text);
			stringBuilder.append(letterShape.applyAnnotations(text));
		}
		stringBuilder.append("\n\n");
		if (paragraph.getHasChildren()) {
			if (deep == null) {
				stringBuilder.append(getChildren(paragraph.getId(), client, 1));
			} else {
				stringBuilder.append(getChildren(paragraph.getId(), client, deep + 1));
			}

		}
		return stringBuilder + "\n";
	}

	private String getChildren(String id, NotionClient client, Integer deep) {
		RetrieveBlockChildrenRequest retrieveBlockChildrenRequest = new RetrieveBlockChildrenRequest(id);
		List<Block> results = client.retrieveBlockChildren(retrieveBlockChildrenRequest).getResults();
		NotionBlockConverter notionBlockConverter = new NotionBlockConverter(deep);
		StringBuilder stringBuilder = new StringBuilder();

		for (Block block : results) {
			for (int i = 0; i < deep; i++) {
				stringBuilder.append("　"); //전각문자가 들어가 있습니다
			}
			stringBuilder.append(notionBlockConverter.doFilter(block, client)).append("\n");
		}
		return stringBuilder.toString();
	}

}
