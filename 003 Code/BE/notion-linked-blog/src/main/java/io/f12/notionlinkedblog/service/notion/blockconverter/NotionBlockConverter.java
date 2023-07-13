package io.f12.notionlinkedblog.service.notion.blockconverter;

import static io.f12.notionlinkedblog.service.notion.blockconverter.type.NotionBlockType.Block.*;

import io.f12.notionlinkedblog.service.notion.blockconverter.filter.BulletedListItemFilter;
import io.f12.notionlinkedblog.service.notion.blockconverter.filter.DivideFilter;
import io.f12.notionlinkedblog.service.notion.blockconverter.filter.HeadingOneFilter;
import io.f12.notionlinkedblog.service.notion.blockconverter.filter.HeadingThreeFilter;
import io.f12.notionlinkedblog.service.notion.blockconverter.filter.HeadingTwoFilter;
import io.f12.notionlinkedblog.service.notion.blockconverter.filter.ParagraphFilter;
import io.f12.notionlinkedblog.service.notion.blockconverter.filter.QuoteFilter;
import io.f12.notionlinkedblog.service.notion.blockconverter.filter.TableFilter;
import io.f12.notionlinkedblog.service.notion.blockconverter.filter.ToDoFilter;
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
		}
		log.info(" - toString()\n{}", stringBuilder.toString());
	}

	@Override
	public String toString() {
		return stringBuilder.toString();
	}
}
