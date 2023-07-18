package io.f12.notionlinkedblog.service.notion.converter.contents.filter;

import io.f12.notionlinkedblog.service.notion.converter.contents.type.NotionBlockType;
import notion.api.v1.NotionClient;
import notion.api.v1.model.blocks.Block;

public class ImageFilter implements NotionFilter {
	@Override
	public boolean isAcceptable(Block block) {
		return block.getType().getValue().equals(NotionBlockType.Block.IMAGE);
	}

	@Override
	public String doFilter(Block block, NotionClient client) {
		String type = block.asImage().getImage().getType();
		String url = null;
		if (type.equals("file")) {
			url = block.asImage().getImage().getFile().getUrl();
		} else {
			url = block.asImage().getImage().getExternal().getUrl();
		}
		return "![]" + "(" + url + ")\n\n";
	}
}
