package io.f12.notionlinkedblog.service.notion.converter.contents.filter;

import java.util.List;

import io.f12.notionlinkedblog.service.notion.converter.contents.CheckAnnotations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import notion.api.v1.model.blocks.Block;
import notion.api.v1.model.pages.PageProperty;

@AllArgsConstructor
@Builder
public class BookmarkFilter {
	private Block block;

	public String doFilter() {
		String url = block.asBookmark().getBookmark().getUrl();
		List<PageProperty.RichText> caption = block.asBookmark().getBookmark().getCaption();

		StringBuilder stringBuilder = new StringBuilder();
		StringBuilder captionStringBuilder = new StringBuilder();

		for (PageProperty.RichText text : caption) {
			CheckAnnotations letterShape = new CheckAnnotations(text);
			captionStringBuilder.append(letterShape.applyAnnotations(text));
		}
		if (captionStringBuilder.toString().equals("")) {
			captionStringBuilder.append("bookmark");
		}

		stringBuilder.append("[").append(captionStringBuilder).append("]");
		stringBuilder.append("(").append(url).append(")");

		stringBuilder.append("\n");
		return stringBuilder.toString();
	}
}
