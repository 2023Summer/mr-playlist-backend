package summer.mrplaylist.member.controller;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.common.config.jwt.JwtProperties;
import summer.mrplaylist.common.dto.JwtTokenDto;
import summer.mrplaylist.common.dto.Response;
import summer.mrplaylist.common.service.JwtTokenProvider;
import summer.mrplaylist.member.dto.AddMemberRequestDto;
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
	private static final String POST_JOIN_MEMBER = "/join";
	private static final String PUT_UPDATE_MEMBER = "/update/{id}";
	private static final String POST_SEND_AUTH_MAIL = "/auth-mail";
	private static final String GET_CHECK_AUTH_CODE = "/auth";
	private static final String POST_REISSUE_ACCESS_TOKEN = "/reissue";

	@PostMapping(POST_JOIN_MEMBER)
	public Response<String> signUp(@RequestBody AddMemberRequestDto requestDto) {
		memberService.join(Member.createMember(requestDto));
		return createMessage("가입 완료");
	}

	@PutMapping(PUT_UPDATE_MEMBER)
	public Response<String> update(@PathVariable Long id,
		@RequestBody UpdateMemberRequestDto requestDto) {
		memberService.update(id, requestDto);
		return createMessage("수정 완료");
	}

	@PostMapping(POST_SEND_AUTH_MAIL)
	public Response<String> sendAuthEmail(@RequestParam String email) {
		try {
			emailService.sendAuthEmail(email);
		} catch (Exception e) {
			return createMessage("이메일 전송 실패");
		}
		return createMessage("이메일 전송 성공");
	}

	@GetMapping(GET_CHECK_AUTH_CODE)
	public Response<String> checkAuth(@RequestParam String email,
		@RequestParam(name = "authnum") String userAuthNumber) {
		if (!emailService.checkAuthNumber(email, userAuthNumber)) {
			return createMessage("이메일 인증 실패");
		}
		return createMessage("이메일 인증 성공");
	}

	@PostMapping(POST_REISSUE_ACCESS_TOKEN)
	public Response reissueAccessToken(@RequestBody JwtTokenDto tokenDto,
		HttpServletRequest request,
		HttpServletResponse response) {
		String newAccessToken = jwtTokenProvider.reissueAccessToken(request);
		response.setHeader("Authorization", JwtProperties.HEADER_STRING + newAccessToken);

		return createMessage(newAccessToken);
	}

	private Response createMessage(String message) {
		return new Response(new Message(message));
	}

}

@Data
@AllArgsConstructor
class Message {
	private String message;
}
