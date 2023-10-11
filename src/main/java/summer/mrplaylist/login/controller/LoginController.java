package summer.mrplaylist.login.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import summer.mrplaylist.common.config.jwt.JwtProperties;
import summer.mrplaylist.common.dto.JwtTokenDto;
import summer.mrplaylist.common.dto.Response;
import summer.mrplaylist.common.util.CurrentUser;
import summer.mrplaylist.login.dto.LoginMemberRequestDto;
import summer.mrplaylist.login.service.LoginService;
import summer.mrplaylist.member.model.Member;

@RestController
@RequiredArgsConstructor
public class LoginController {

	private final LoginService loginService;
	private static final String POST_LOGIN = "/login";

	private static final String POST_LOGOUT = "/logout";
	private static final String POST_OAUTH2_LOGIN = "/login/oauth2";

	@PostMapping(POST_LOGIN)
	public Response<JwtTokenDto> postLogin(@RequestBody LoginMemberRequestDto requestDto,
		HttpServletResponse response) {
		JwtTokenDto tokenDto = loginService.login(requestDto);
		response.setHeader("Authorization", JwtProperties.HEADER_STRING + tokenDto.getAccessToken());
		response.setHeader("RefreshToken", JwtProperties.HEADER_STRING + tokenDto.getAccessToken());
		return new Response<>(tokenDto);
	}

	@GetMapping(POST_LOGOUT)
	public ResponseEntity<String> postLogout(@RequestBody JwtTokenDto jwtTokenDto, @CurrentUser Member member) {
		loginService.logout(jwtTokenDto, member.getEmail());
		return ResponseEntity.ok("로그아웃");
	}

}
