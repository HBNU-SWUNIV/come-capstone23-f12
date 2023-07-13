package io.f12.notionlinkedblog.service.notion.blockconverter;

import io.f12.notionlinkedblog.service.notion.blockconverter.filter.BulletedListItemFilter;
import io.f12.notionlinkedblog.service.notion.blockconverter.filter.HeadingOneFilter;
import io.f12.notionlinkedblog.service.notion.blockconverter.filter.HeadingThreeFilter;
import io.f12.notionlinkedblog.service.notion.blockconverter.filter.HeadingTwoFilter;
import io.f12.notionlinkedblog.service.notion.blockconverter.filter.ParagraphFilter;
import io.f12.notionlinkedblog.service.notion.blockconverter.filter.TableFilter;
import lombok.extern.slf4j.Slf4j;
import notion.api.v1.NotionClient;
import notion.api.v1.model.blocks.Block;

@Slf4j
public class NotionBlockConverter {

	private StringBuilder stringBuilder = new StringBuilder();
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
		if (type == "paragraph") {
			stringBuilder.append(ParagraphFilter.builder().block(block).build().doFilter());
		} else if (type == "heading_1") {
			stringBuilder.append(HeadingOneFilter.builder().block(block).build().doFilter());
		} else if (type == "heading_2") {
			stringBuilder.append(HeadingTwoFilter.builder().block(block).build().doFilter());
		} else if (type == "heading_3") {
			stringBuilder.append(HeadingThreeFilter.builder().block(block).build().doFilter());
		} else if (type == "bulleted_list_item") {
			stringBuilder.append(BulletedListItemFilter.builder().block(block).build().doFilter());
		} else if (type == "table") {
			stringBuilder.append(TableFilter.builder().block(block).notionClient(notionClient).build().doFilter());
		}
		log.info(" - toString()\n{}", stringBuilder.toString());
	}

	@Override
	public String toString() {
		return stringBuilder.toString();
	}
}
