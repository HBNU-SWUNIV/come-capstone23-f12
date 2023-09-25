package io.f12.notionlinkedblog.notion.api;

import static io.f12.notionlinkedblog.common.exceptions.message.ExceptionMessages.UserValidateMessages.*;
import static org.springframework.util.MimeTypeUtils.*;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.f12.notionlinkedblog.common.Endpoint;
import io.f12.notionlinkedblog.common.domain.CommonErrorResponse;
import io.f12.notionlinkedblog.common.exceptions.exception.NotionAuthenticationException;
import io.f12.notionlinkedblog.notion.api.port.NotionService;
import io.f12.notionlinkedblog.notion.domain.dto.CreateNotionPageToBlogDto;
import io.f12.notionlinkedblog.post.api.response.PostSearchDto;
import io.f12.notionlinkedblog.security.login.ajax.dto.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Notion", description = "노션 연동 API")
@RestController
@RequestMapping(Endpoint.Api.NOTION)
@RequiredArgsConstructor
public class NotionApiController {

	private final NotionService notionService;

	@PostMapping("/single")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "노션 포스트 생성 - single", description = "노션에서 포스트 1개를 가져와 포스트를 생성합니다. 기본 생성모드는 공개입니다")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "포스트 생성 성공",
			content = @Content(mediaType = APPLICATION_JSON_VALUE,
				schema = @Schema(implementation = PostSearchDto.class,
					description = "썸네일, 설명, 시리즈는 연동 이후 따로 등록해야 합니다."))),
		@ApiResponse(responseCode = "400", description = "isPublic 값이 0, 1 이 아닌경우",
			content = @Content(mediaType = APPLICATION_JSON_VALUE,
				schema = @Schema(implementation = CommonErrorResponse.class)))
	})
	public PostSearchDto getSingleNotionPageToBlog(
		@Parameter(hidden = true) @AuthenticationPrincipal LoginUser loginUser,
		@RequestBody @Validated CreateNotionPageToBlogDto notionToBlogDto) throws NotionAuthenticationException {
		notionAccessAvailable(loginUser);
		return notionService.saveSingleNotionPage(notionToBlogDto.getPath(), loginUser.getUser().getId());
	}

	private void notionAccessAvailable(LoginUser loginUser) {
		Assert.notNull(loginUser.getUser().getNotionOauth().getAccessToken(), "AuthenticationNeed");
	}
	//
	// @PostMapping("/setpage")
	// public void setPage(@Parameter(hidden = true) @AuthenticationPrincipal LoginUser loginUser,
	// 	@RequestPart(value = "pageId") String pageId) {
	// 	notionService.setNotionLinkPages(pageId, loginUser.getUser().getId());
	// }
	//
	// @PutMapping
	// @ResponseStatus(HttpStatus.OK)
	// @Operation(summary = "Notion 연동 포스트 수정", description = "id 에 해당하는 포스트 수정")
	// @ApiResponses(value = {
	// 	@ApiResponse(responseCode = "200", description = "포스트 수정 성공",
	// 		content = @Content(mediaType = APPLICATION_JSON_VALUE,
	// 			schema = @Schema(implementation = PostSearchDto.class,
	// 				description = "PostId 에 해당하는 사용자의 PostEntity 를 노션과 업데이트, 업데이트시 모든 내용을 대체")))
	// })
	// public PostSearchDto updateNotionPageToBlog(@Parameter(hidden = true) @AuthenticationPrincipal LoginUser loginUser,
	// 	@RequestBody @Validated UpdateNotionPageInfoDto infoDto) throws NotionAuthenticationException {
	// 	checkSameUser(infoDto.getUserId(), loginUser);
	// 	return notionService.editNotionPageToBlogDev(infoDto.getUserId(), infoDto.getPostId());
	// }

	private void checkSameUser(Long id, LoginUser loginUser) {
		if (!id.equals(loginUser.getUser().getId())) {
			throw new AccessDeniedException(USER_NOT_MATCH);
		}
	}

}
