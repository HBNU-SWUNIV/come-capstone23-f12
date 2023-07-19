package io.f12.notionlinkedblog.service.notion.converter.contents.filter;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

import io.f12.notionlinkedblog.service.notion.converter.contents.type.NotionBlockType;
import notion.api.v1.NotionClient;
import notion.api.v1.model.blocks.Block;

public class FileFilter implements NotionFilter {
	@Override
	public boolean isAcceptable(Block block) {
		return block.getType().getValue().equals(NotionBlockType.Block.FILE);
	}

	@Override
	public String doFilter(Block block, NotionClient client) {
		String url = block.asFile().getFile().getFile().getUrl();
		String systemPath = System.getProperty("user.dir");
		File file = null;
		String fileName = urlToFileName(url);

		try {
			file = new File(systemPath + "\\" + fileName);
			FileUtils.copyURLToFile(new URL(url), file);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		//TODO: 내부 저장되어있는 파일을 url 로 변경시켜주는 메소드 필요
		return "[" + fileName + "]" + "(" + url + ")" + "\n\n";
	}

	private String urlToFileName(String urlString) {
		String[] split1 = urlString.split("/");
		String second = split1[split1.length - 1];

		String[] split2 = second.split("\\?");
		String encodedName = split2[0];

		return URLDecoder.decode(encodedName, StandardCharsets.UTF_8);
	}
}
