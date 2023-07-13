package io.f12.notionlinkedblog.service.notion.blockconverter.filter;

import java.util.List;

import io.f12.notionlinkedblog.service.notion.blockconverter.CheckAnnotations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import notion.api.v1.model.blocks.Block;
import notion.api.v1.model.pages.PageProperty;

@AllArgsConstructor
@Builder
public class HeadingThreeFilter {
	private Block block;

	public String doFilter() {
		List<PageProperty.RichText> texts = block.asHeadingThree().getHeading3().getRichText();
		StringBuilder stringBuilder = new StringBuilder();
		for (PageProperty.RichText text : texts) {
			CheckAnnotations letterShape = new CheckAnnotations(text);
			stringBuilder.append(letterShape.applyAnnotations(text));
		}
		return toHeadingThree(stringBuilder);
	}

	private String toHeadingThree(StringBuilder sb) {
		return "### " + sb.toString() + "\n";
	}
}