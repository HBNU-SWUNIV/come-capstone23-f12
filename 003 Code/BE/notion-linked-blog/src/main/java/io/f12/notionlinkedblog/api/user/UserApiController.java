package io.f12.notionlinkedblog.api.user;

import static io.f12.notionlinkedblog.api.EmailApiController.*;

import java.net.URI;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.f12.notionlinkedblog.domain.user.dto.info.UserSearchDto;
import io.f12.notionlinkedblog.domain.user.dto.signup.UserSignupRequestDto;
import io.f12.notionlinkedblog.domain.user.dto.signup.UserSignupResponseDto;
import io.f12.notionlinkedblog.service.user.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserApiController {

	private final UserService userService;

	@PostMapping("/email/signup")
	public ResponseEntity<UserSignupResponseDto> signupByEmail(
		@RequestBody @Validated UserSignupRequestDto requestDto, BindingResult bindingResult, HttpSession httpSession) {
		verifyEmailIsVerifiedElseThrowIllegalStateException(httpSession);

		Long savedId = userService.signupByEmail(requestDto);
		UserSignupResponseDto responseDto = UserSignupResponseDto.builder().id(savedId).build();
		httpSession.removeAttribute(emailVerifiedAttr);

		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}

	private void verifyEmailIsVerifiedElseThrowIllegalStateException(HttpSession session) {
		String isVerified = (String)session.getAttribute(emailVerifiedAttr);
		if (isVerified == null) {
			throw new IllegalStateException("이메일 검증이 되지 않았습니다.");
		}
	}

	@GetMapping(value = "/{id}")
	public UserSearchDto getUserInfo(@PathVariable Long id) {
		return userService.getUserInfo(id);
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<?> editUserInfo(
		HttpSession session,
		@PathVariable Long id,
		@RequestParam(value = "username", required = false) String username,
		@RequestParam(value = "email", required = false) String email,
		@RequestParam(value = "password", required = false) String password,
		@RequestParam(value = "profile", required = false) String profile,
		@RequestParam(value = "blogTitle", required = false) String blogTitle,
		@RequestParam(value = "githubLink", required = false) String githubLink,
		@RequestParam(value = "instagramLink", required = false) String instagramLink,
		@RequestParam(value = "introduction", required = false) String introduction
	) {
		Optional<UserSearchDto> sessionUser = Optional.ofNullable((UserSearchDto)session.getAttribute(session.getId()));
		if (sessionUser.isPresent() && sessionUser.get().getId().equals(id)) {
			userService.editUserInfo(id, username, email, password, profile, blogTitle,
				githubLink, instagramLink, introduction);
		}
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(URI.create("/api/users/" + id));
		return new ResponseEntity<>(httpHeaders, HttpStatus.FOUND);
	}

	@DeleteMapping("/{id}")
	public String deleteUserInfo(@PathVariable Long id, HttpSession session) {
		Optional<UserSearchDto> sessionUser = Optional.ofNullable((UserSearchDto)session.getAttribute(session.getId()));
		if (sessionUser.isPresent() && sessionUser.get().getId().equals(id)) {
			userService.removeUser(id);
		}
		return "done";
	}

	/**
	 * 테스트용  api
	 * @param id id에 해당하는 세션 가져오기
	 */

	@GetMapping("/getSession/{id}")
	private String getSessionForTest(HttpSession session, @PathVariable Long id) {
		UserSearchDto userInfo = userService.getUserInfo(id);
		String sessionId = session.getId();
		session.setAttribute(sessionId, userInfo);
		return sessionId;
	}
}
