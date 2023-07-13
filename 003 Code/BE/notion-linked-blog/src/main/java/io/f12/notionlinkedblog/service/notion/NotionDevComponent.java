package io.f12.notionlinkedblog.service.notion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Service
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotionDevComponent {
	@Value("${external.notionSecret}")
	private String internalSecret;
}
