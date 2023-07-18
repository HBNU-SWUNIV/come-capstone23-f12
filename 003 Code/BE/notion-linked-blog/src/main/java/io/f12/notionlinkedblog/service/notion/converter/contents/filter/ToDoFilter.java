package io.f12.notionlinkedblog.service.notion.converter.contents.filter;

import java.util.List;

import io.f12.notionlinkedblog.service.notion.converter.contents.CheckAnnotations;
import io.f12.notionlinkedblog.service.notion.converter.contents.type.NotionBlockType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import notion.api.v1.NotionClient;
import notion.api.v1.model.blocks.Block;
import notion.api.v1.model.pages.PageProperty;

@AllArgsConstructor
@Builder
public class ToDoFilter implements NotionFilter {

	@Override
	public boolean isAcceptable(Block block) {
		return block.getType().getValue().equals(NotionBlockType.Block.TODO);
	}

	@Override
	public String doFilter(Block block, NotionClient client) {
		boolean isChecked = block.asToDo().getToDo().getChecked();
		List<PageProperty.RichText> texts = block.asToDo().getToDo().getRichText();
		StringBuilder stringBuilder = new StringBuilder();

		for (PageProperty.RichText text : texts) {
			CheckAnnotations letterShape = new CheckAnnotations(text);
			if (isChecked) {
				stringBuilder.append(" - [x] ");
			} else {
				stringBuilder.append(" - [ ] ");
			}
			stringBuilder.append(letterShape.applyAnnotations(text));
		}
		return stringBuilder + "\n";
	}
}
