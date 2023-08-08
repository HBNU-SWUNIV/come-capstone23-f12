package io.f12.notionlinkedblog.entity.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PagingInfo {
	private Integer pageNow;
	private Integer elementSize;
}
