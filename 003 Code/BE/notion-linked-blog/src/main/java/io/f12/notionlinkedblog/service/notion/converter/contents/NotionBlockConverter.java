package io.f12.notionlinkedblog.service.notion.converter.contents;

import static io.f12.notionlinkedblog.service.notion.converter.contents.type.NotionBlockType.Block.*;

import io.f12.notionlinkedblog.service.notion.converter.contents.filter.BookmarkFilter;
import io.f12.notionlinkedblog.service.notion.converter.contents.filter.BulletedListItemFilter;
import io.f12.notionlinkedblog.service.notion.converter.contents.filter.CodeBlockFilter;
import io.f12.notionlinkedblog.service.notion.converter.contents.filter.DivideFilter;
import io.f12.notionlinkedblog.service.notion.converter.contents.filter.HeadingOneFilter;
import io.f12.notionlinkedblog.service.notion.converter.contents.filter.HeadingThreeFilter;
import io.f12.notionlinkedblog.service.notion.converter.contents.filter.HeadingTwoFilter;
import io.f12.notionlinkedblog.service.notion.converter.contents.filter.NumberedListItemFilter;
import io.f12.notionlinkedblog.service.notion.converter.contents.filter.ParagraphFilter;
import io.f12.notionlinkedblog.service.notion.converter.contents.filter.QuoteFilter;
import io.f12.notionlinkedblog.service.notion.converter.contents.filter.TableFilter;
import io.f12.notionlinkedblog.service.notion.converter.contents.filter.ToDoFilter;
import io.f12.notionlinkedblog.service.notion.converter.contents.filter.ToggleBlockFilter;
import lombok.extern.slf4j.Slf4j;
import notion.api.v1.NotionClient;
import notion.api.v1.model.blocks.Block;

@Slf4j
public class NotionBlockConverter {

	private final StringBuilder stringBuilder = new StringBuilder();
	private Block block;
	private String type;
	private NotionClient notionClient;

	public void setType(Block block) {
		this.block = block;
		this.type = block.getType().getValue();
	}

	public void setNotionClient(NotionClient notionClient) {
		this.notionClient = notionClient;
	}

	public void doFilter() {
		if (type == PARAGRAPH) {
			stringBuilder.append(ParagraphFilter.builder().block(block).build().doFilter());
		} else if (type == H1) {
			stringBuilder.append(HeadingOneFilter.builder().block(block).build().doFilter());
		} else if (type == H2) {
			stringBuilder.append(HeadingTwoFilter.builder().block(block).build().doFilter());
		} else if (type == H3) {
			stringBuilder.append(HeadingThreeFilter.builder().block(block).build().doFilter());
		} else if (type == BULLETED_LIST_ITEM) {
			stringBuilder.append(BulletedListItemFilter.builder().block(block).build().doFilter());
		} else if (type == TABLE) {
			stringBuilder.append(TableFilter.builder().block(block).notionClient(notionClient).build().doFilter());
		} else if (type == QUOTE) {
			stringBuilder.append(QuoteFilter.builder().block(block).build().doFilter());
		} else if (type == DIVIDER) {
			stringBuilder.append(DivideFilter.builder().build().doFilter());
		} else if (type == TODO) {
			stringBuilder.append(ToDoFilter.builder().block(block).build().doFilter());
		} else if (type == NUMBERED_LIST_ITEM) {
			stringBuilder.append(NumberedListItemFilter.builder().block(block).build().doFilter());
		} else if (type == CODE_BLOCK) {
			stringBuilder.append(CodeBlockFilter.builder().block(block).build().doFilter());
		} else if (type == BOOKMARK) {
			stringBuilder.append(BookmarkFilter.builder().block(block).build().doFilter());
		} else if (type == TOGGLE_BLOCK) {
			stringBuilder.append(
				ToggleBlockFilter.builder().block(block).notionClient(notionClient).build().doFilter());
		}
		log.info(" - toString()\n{}", stringBuilder);
	}

	@Override
	public String toString() {
		return stringBuilder.toString();
	}
}
