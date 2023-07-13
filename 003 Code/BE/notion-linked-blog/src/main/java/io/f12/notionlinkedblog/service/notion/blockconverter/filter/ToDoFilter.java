package io.f12.notionlinkedblog.service.notion.blockconverter.filter;

import java.util.List;

import io.f12.notionlinkedblog.service.notion.blockconverter.CheckAnnotations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import notion.api.v1.model.blocks.Block;
import notion.api.v1.model.pages.PageProperty;

@AllArgsConstructor
@Builder
public class ToDoFilter {
	private Block block;

	public String doFilter() {
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
		return stringBuilder.toString() + "\n";
	}
}
