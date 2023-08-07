package summer.mrplaylist.common.config.jwt;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.common.dto.JwtTokenDto;
import summer.mrplaylist.common.service.JwtTokenProvider;
import summer.mrplaylist.common.service.RedisService;
import summer.mrplaylist.member.model.Member;

@SpringBootTest
@Slf4j
class JwtTokenProviderTest {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private RedisService redisService;
	String email = "test@naver.com";
	String password = "1234";
	String nickname = "hello";
	Member member;

	@BeforeEach
	public void init() {
		//given
		member = Member.builder()
			.email(email)
			.password(password)
			.nickname(nickname)
			.build();
	}

	@Test
	public void refreshTokenIssueTest() throws Exception {

		//when
		JwtTokenDto jwtTokenDto = jwtTokenProvider.createAllToken(member);
		String dbRefreshToken = redisService.getData(member.getEmail());

		//then
		assertThat(jwtTokenDto.getRefreshToken()).isEqualTo(dbRefreshToken);
	}

	@Test
	public void tokenValidTest() throws Exception {
		//given
		String secretKey = "SlNPTl9XRUJfVE9LRU5fVEVTVF9TRUNSRVRfS0VZUw==";
		//when
		String accessToken = jwtTokenProvider.createAccessToken(member);
		//then
		Jwts.parser()
			.setSigningKey(secretKey)
			.parseClaimsJws(accessToken);
	}

	@Test
	public void getEmailTest() throws Exception {
		//when
		String accessToken = jwtTokenProvider.createAccessToken(member);
		String tokenEmail = jwtTokenProvider.getEmail(accessToken);
		//then
		assertThat(tokenEmail).isEqualTo(email);
	}

	@Test
	public void reissueAccessToken() throws Exception {

	}

}
