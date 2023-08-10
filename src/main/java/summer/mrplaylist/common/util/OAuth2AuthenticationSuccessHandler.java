package summer.mrplaylist.common.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import summer.mrplaylist.common.dto.JwtTokenDto;
import summer.mrplaylist.common.model.UserPrincipal;
import summer.mrplaylist.common.service.JwtTokenProvider;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final JwtTokenProvider jwtTokenProvider;
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {

		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		// 토큰 발급
		JwtTokenDto jwtTokenDto = jwtTokenProvider.createAllToken(userPrincipal.getMember());

		String targetUrl = makeRedirectUrl(jwtTokenDto);
		log.info("targetUrl : {}", targetUrl);
		if (response.isCommitted()) {
			logger.debug("응답이 이미 커밋된 상태입니다. " + targetUrl + "로 리다이렉트하도록 바꿀 수 없습니다.");
			return;
		}
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	private String makeRedirectUrl(JwtTokenDto jwtTokenDto) {
		return UriComponentsBuilder.fromUriString("/")
			.queryParam("token", jwtTokenDto)
			.build().toUriString();
	}

}
