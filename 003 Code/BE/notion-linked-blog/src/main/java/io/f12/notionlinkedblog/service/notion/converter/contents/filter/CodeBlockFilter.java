package io.f12.notionlinkedblog.service.notion.converter.contents.filter;

import java.util.List;

import io.f12.notionlinkedblog.service.notion.converter.contents.CheckAnnotations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import notion.api.v1.model.blocks.Block;
import notion.api.v1.model.blocks.CodeBlock;
import notion.api.v1.model.pages.PageProperty;

@AllArgsConstructor
@Builder
public class CodeBlockFilter {
	private Block block;

	public String doFilter() {
		CodeBlock.Element codeBlock = block.asCode().getCode();
		List<PageProperty.RichText> texts = codeBlock.getRichText();
		String codeLanguage = codeBlock.getLanguage();
		// List<PageProperty.RichText> caption = codeBlock.getCaption();
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("```").append(codeLanguage).append("\n");

		for (PageProperty.RichText text : texts) {
			CheckAnnotations letterShape = new CheckAnnotations(text);
			stringBuilder.append(letterShape.applyAnnotations(text));
		}

		stringBuilder.append("\n").append("```");

		stringBuilder.append("\n");
		return stringBuilder.toString();
	}
}
