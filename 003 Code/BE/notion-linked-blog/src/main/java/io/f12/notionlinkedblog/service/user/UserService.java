package io.f12.notionlinkedblog.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.f12.notionlinkedblog.domain.user.User;
import io.f12.notionlinkedblog.domain.user.dto.info.UserSearchDto;
import io.f12.notionlinkedblog.domain.user.dto.signup.UserSignupRequestDto;
import io.f12.notionlinkedblog.repository.user.UserDataRepository;
import io.f12.notionlinkedblog.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

	private final UserRepository userRepository;
	private final UserDataRepository userDataRepository;
	private final PasswordEncoder passwordEncoder;

	public Long signupByEmail(UserSignupRequestDto requestDto) {
		checkEmailIsDuplicated(requestDto.getEmail());

		requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
		User newUser = requestDto.toEntity();
		User savedUser = userRepository.save(newUser);

		return savedUser.getId();
	}

	@Transactional(readOnly = true)
	public UserSearchDto getUserInfo(Long id) {
		return userDataRepository.findUserByDto(id).orElse(new UserSearchDto());
	}

	public String editUserInfo(Long id, String username, String email, String password,
		String profile, String blogTitle, String githubLink,
		String instagramLink, String introduction) {

		User findUser = userDataRepository.findById(id).orElse(null);
		if (findUser == null) {
			return "error";
		} else {
			findUser.editProfile(username, email, password, profile,
				blogTitle, githubLink, instagramLink, introduction);
			return "success";
		}
	}

	public String removeUser(Long id) {
		userDataRepository.deleteById(id);
		return "success";
	}

	private void checkEmailIsDuplicated(final String email) {
		boolean isPresent = userRepository.findByEmail(email).isPresent();
		if (isPresent) {
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}
	}
}
