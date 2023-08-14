package summer.mrplaylist.common.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import summer.mrplaylist.common.config.jwt.ExceptionHandlerFilter;
import summer.mrplaylist.common.config.jwt.JwtAuthenticationFilter;
import summer.mrplaylist.common.service.CustomOAuth2UserService;
import summer.mrplaylist.common.service.JwtTokenProvider;
import summer.mrplaylist.common.util.OAuth2AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;
	private final ExceptionHandlerFilter exceptionHandlerFilter;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.httpBasic(AbstractHttpConfigurer::disable)
			.csrf(CsrfConfigurer::disable)
			.formLogin(FormLoginConfigurer::disable)
			.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(
				SessionCreationPolicy.STATELESS)) // 세션 미사용
			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
			.authorizeHttpRequests(request -> request
				.requestMatchers("/login/**", "/api/members/**").permitAll()
				.anyRequest().authenticated())
			.addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class);

		http.oauth2Login(oauth2 -> oauth2
			.successHandler(oAuth2AuthenticationSuccessHandler)
			.userInfoEndpoint(
				userInfoEndpointConfig -> userInfoEndpointConfig
					.userService(customOAuth2UserService))

		);

		return http.build();

	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
