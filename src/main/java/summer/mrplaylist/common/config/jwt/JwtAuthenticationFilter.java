package summer.mrplaylist.common.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import summer.mrplaylist.common.service.JwtTokenProvider;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String token = jwtTokenProvider.preprocessingToken(request);
		if (StringUtils.hasText(token) && jwtTokenProvider.validToken(token)) {
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} else if (StringUtils.hasText(token) && !jwtTokenProvider.validToken(token)) {
			jwtTokenProvider.reissueAccessToken(request);
		}
		filterChain.doFilter(request, response);
	}
}
