package io.f12.notionlinkedblog.service.notion.blockconverter.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class DivideFilter {
	public String doFilter() {
		return "---\n";
	}
}
