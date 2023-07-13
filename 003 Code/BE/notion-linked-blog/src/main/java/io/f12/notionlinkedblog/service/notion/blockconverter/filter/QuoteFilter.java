package io.f12.notionlinkedblog.service.notion.blockconverter.filter;

import java.util.List;

import org.springframework.util.StringUtils;

import io.f12.notionlinkedblog.service.notion.blockconverter.CheckAnnotations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import notion.api.v1.model.blocks.Block;
import notion.api.v1.model.pages.PageProperty;

@AllArgsConstructor
@Builder
public class QuoteFilter {
	private Block block;

	public String doFilter() {
		List<PageProperty.RichText> texts = block.asQuote().getQuote().getRichText();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("> ");

		for (PageProperty.RichText text : texts) {
			CheckAnnotations letterShape = new CheckAnnotations(text);
			stringBuilder.append(letterShape.applyAnnotations(text));
		}
		String replace = StringUtils.replace(stringBuilder.toString(), "\n", "</br>");
		return replace + "\n\n";
	}
}
