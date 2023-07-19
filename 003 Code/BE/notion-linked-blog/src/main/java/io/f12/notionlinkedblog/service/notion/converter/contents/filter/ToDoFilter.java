package io.f12.notionlinkedblog.service.notion.converter.contents.filter;

import java.util.List;

import io.f12.notionlinkedblog.service.notion.converter.contents.CheckAnnotations;
import io.f12.notionlinkedblog.service.notion.converter.contents.ChildrenConverter;
import io.f12.notionlinkedblog.service.notion.converter.contents.type.NotionBlockType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import notion.api.v1.NotionClient;
import notion.api.v1.model.blocks.Block;
import notion.api.v1.model.blocks.ToDoBlock;
import notion.api.v1.model.pages.PageProperty;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ToDoFilter implements NotionFilter {
	private Integer deep;

	@Override
	public boolean isAcceptable(Block block) {
		return block.getType().getValue().equals(NotionBlockType.Block.TODO);
	}

	@Override
	public String doFilter(Block block, NotionClient client) {
		ToDoBlock toDo = block.asToDo();
		boolean isChecked = toDo.getToDo().getChecked();
		List<PageProperty.RichText> texts = toDo.getToDo().getRichText();
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
		stringBuilder.append("  ").append("\n");
		stringBuilder.append(getChildren(client, toDo));
		return stringBuilder + "\n";
	}

	private String getChildren(NotionClient client, ToDoBlock toDoBlock) {
		StringBuilder stringBuilder = new StringBuilder();
		if (toDoBlock.getHasChildren()) {
			if (deep == null) {
				ChildrenConverter childrenConverter = new ChildrenConverter(toDoBlock.getId(), client, 1);
				stringBuilder.append(childrenConverter.getCommonChildren());
			} else {
				ChildrenConverter childrenConverter = new ChildrenConverter(toDoBlock.getId(), client, deep + 1);
				stringBuilder.append(childrenConverter.getCommonChildren());
			}
		}
		return stringBuilder.toString();
	}
}
