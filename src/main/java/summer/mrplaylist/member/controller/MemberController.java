package summer.mrplaylist.member.controller;

import static summer.mrplaylist.member.controller.MemberController.Message.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.common.config.jwt.JwtProperties;
import summer.mrplaylist.common.dto.JwtTokenDto;
import summer.mrplaylist.common.dto.Response;
import summer.mrplaylist.common.service.JwtTokenProvider;
import summer.mrplaylist.common.util.CurrentUser;
import summer.mrplaylist.member.dto.AddMemberRequestDto;
import summer.mrplaylist.member.dto.MemberResponse;
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
	private static final String GET_MEMBER_INFO = "/info";

	@PostMapping(POST_JOIN_MEMBER)
	public Response signUp(@RequestBody AddMemberRequestDto requestDto) {
		Member member = memberService.join(Member.createMember(requestDto));
		return createResponse(member, "회원 가입이 완료되었습니다.");
	}

	@PostMapping(PUT_UPDATE_MEMBER)
	public Response update(@RequestBody UpdateMemberRequestDto requestDto) {
		Member member = memberService.update(requestDto);
		return createResponse(member, "회원정보 수정이 완료되었습니다.");
	}

	@PostMapping(POST_SEND_AUTH_MAIL)
	public Response sendAuthEmail(@RequestParam String email) {
		try {
			emailService.sendAuthEmail(email);
		} catch (Exception e) {
			return new Response("이메일 전송을 실패하였습니다.");
		}
		return new Response("이메일 전송을 성공하였습니다.");
	}

	@GetMapping(GET_CHECK_AUTH_CODE)
	public Response checkAuth(@RequestParam String email,
		@RequestParam(name = "authnum") String userAuthNumber) {
		if (!emailService.checkAuthNumber(email, userAuthNumber)) {
			return new Response("이메일 인증에 실패하였습니다.");
		}
		return new Response("이메일 인증을 성공하였습니다.");
	}

	@PostMapping(POST_REISSUE_ACCESS_TOKEN)
	public Response reissueAccessToken(@RequestBody JwtTokenDto tokenDto,
		HttpServletRequest request,
		HttpServletResponse response) {
		String newAccessToken = jwtTokenProvider.reissueAccessToken(request);
		response.setHeader("Authorization", JwtProperties.HEADER_STRING + newAccessToken);

		return new Response("액세스 토큰 발급을 완료하였습니다.");
	}

	@GetMapping(GET_MEMBER_INFO)
	public Response getMemberInfo(@CurrentUser Member member) {
		return new Response(MemberResponse.toResponse(member));
	}

	@Data
	@AllArgsConstructor
	@Builder
	static class Message {
		private Long id;
		private String type;
		private String message;

		public static Response createResponse(Member member, String message) {
			Message msg = builder()
				.id(member.getId())
				.type("member")
				.message(message)
				.build();
			return new Response(msg);
		}
	}

}

