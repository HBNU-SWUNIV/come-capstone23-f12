package io.f12.notionlinkedblog.service.notion.converter.contents.filter;

import io.f12.notionlinkedblog.service.notion.converter.contents.type.NotionBlockType;
import notion.api.v1.NotionClient;
import notion.api.v1.model.blocks.Block;

public class LinkPreviewFilter implements NotionFilter {
	@Override
	public boolean isAcceptable(Block block) {
		return block.getType().getValue().equals(NotionBlockType.Block.LINK_PREVIEW);
	}

	@Override
	public String doFilter(Block block, NotionClient client) {
		String url = block.asLinkPreview().getLinkPreview().getUrl();

		StringBuilder stringBuilder = new StringBuilder();
		StringBuilder captionStringBuilder = new StringBuilder();

		//TODO: 이름 어떻게 대체할 것인지

		captionStringBuilder.append("LinkPreview");

		stringBuilder.append("[").append(captionStringBuilder).append("]");
		stringBuilder.append("(").append(url).append(")");

		stringBuilder.append("\n\n");
		return stringBuilder.toString();
	}
}
