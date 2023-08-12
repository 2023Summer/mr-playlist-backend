package summer.mrplaylist.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.common.config.jwt.JwtProperties;
import summer.mrplaylist.common.dto.JwtTokenDto;
import summer.mrplaylist.common.dto.Response;
import summer.mrplaylist.common.service.JwtTokenProvider;
import summer.mrplaylist.member.dto.AddMemberRequestDto;
import summer.mrplaylist.member.dto.LoginMemberRequestDto;
import summer.mrplaylist.member.dto.UpdateMemberRequestDto;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.member.service.EmailService;
import summer.mrplaylist.member.service.MemberService;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/members")
public class MemberController {

	private final MemberService memberService;
	private final JwtTokenProvider jwtTokenProvider;
	private final EmailService emailService;

	@PostMapping("/join")
	public ResponseEntity<String> signUp(@RequestBody AddMemberRequestDto requestDto) {
		memberService.join(Member.createMember(requestDto));
		return ResponseEntity.ok("가입 완료");
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> update(@PathVariable Long id,
		@RequestBody UpdateMemberRequestDto requestDto) {
		memberService.update(id, requestDto);
		return Response
	}

	@PostMapping("/auth")
	public ResponseEntity<Boolean> sendAuthEmail(@RequestParam String email) {
		try {
			emailService.sendAuthEmail(email);
		} catch (Exception e) {
			return ResponseEntity.ok(false);
		}
		return ResponseEntity.ok(true);
	}

	@GetMapping("/auth")
	public ResponseEntity<Boolean> checkAuth(@RequestParam String email,
		@RequestParam(name = "authnum") String userAuthNumber) {
		if (emailService.checkAuthNumber(email, userAuthNumber)) {
			return ResponseEntity.ok(true);
		}
		return ResponseEntity.ok(false);
	}

	@PostMapping("/login")
	public ResponseEntity<JwtTokenDto> login(@RequestBody LoginMemberRequestDto requestDto,
		HttpServletResponse response) {
		JwtTokenDto tokenDto = memberService.login(requestDto);
		response.setHeader("Authorization", JwtProperties.HEADER_STRING + tokenDto.getAccessToken());
		response.setHeader("RefreshToken", JwtProperties.HEADER_STRING + tokenDto.getAccessToken());
		return ResponseEntity.ok(tokenDto);
	}

	@GetMapping("/login")
	public ResponseEntity<String> hi() {
		return ResponseEntity.ok("hi");
	}

	@PostMapping("/reissue")
	public ResponseEntity<String> reissueAccessToken(@RequestBody JwtTokenDto tokenDto,
		HttpServletRequest request,
		HttpServletResponse response) {
		String newAccessToken = jwtTokenProvider.reissueAccessToken(request);
		response.setHeader("Authorization", JwtProperties.HEADER_STRING + newAccessToken);

		return ResponseEntity.ok(newAccessToken);
	}

	@GetMapping("/login/oauth2/code/{registartionId}")
	public ResponseEntity<String> googleLoginTest(@RequestParam String code) {
		log.info("code : {}", code);
		//		log.info("registrationId: {}", registrationId);
		return ResponseEntity.ok("hi");
	}

}
