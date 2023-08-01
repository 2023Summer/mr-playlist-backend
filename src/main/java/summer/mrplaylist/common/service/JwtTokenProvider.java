package summer.mrplaylist.common.service;


import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import summer.mrplaylist.common.config.jwt.JwtProperties;
import summer.mrplaylist.common.constant.JwtTokenConstants;
import summer.mrplaylist.common.dto.JwtTokenDto;
import summer.mrplaylist.common.service.RedisService;
import summer.mrplaylist.common.service.UserDetailsServiceImpl;
import summer.mrplaylist.member.constant.MemberConstants;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.member.repository.MemberRepository;

import java.util.Date;

@RequiredArgsConstructor
@Service
@Slf4j
public class JwtTokenProvider {

    private final UserDetailsServiceImpl userDetailsService;
    private final RedisService redisService;
    private final MemberRepository memberRepository;
    private final JwtProperties jwtProperties;

    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    public JwtTokenDto createAllToken(Member member) {
        return JwtTokenDto.createJwtTokenDto(createAccessToken(member), createRefreshToken(member));

    }
    public String createAccessToken(Member member) {

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ jwtProperties.getAccessTokenExpiration()))
                .setSubject(member.getEmail())
                .claim("id", member.getId())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public String createRefreshToken(Member member) {
        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ jwtProperties.getRefreshTokenExpiration()))
                .setSubject(member.getEmail())
                .claim("id", member.getId())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();

        redisService.setDataWithExpire(
                member.getEmail(),
                refreshToken,
                600L
        );

        return refreshToken;
    }

    public boolean validToken(String token){
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e){
            log.error("exception :" + e);
            return false;
        }
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    public String getEmail(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String preprocessingToken(HttpServletRequest request){
        String bearerToken = request.getHeader(jwtProperties.HEADER_STRING);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtProperties.getSecretKey())){
            return bearerToken.substring(7);
        }
        return null;
    }

    public String reissueAccessToken(HttpServletRequest request) {
        String requestRefreshToken = request.getHeader("RefreshToken");

        String userEmail = getAuthentication(requestRefreshToken).getName();
        String targetRefreshToken = redisService.getData(userEmail); // 회원 이메일로 저장된 토큰과 값 비교
        log.info("tg : {}, req : {}", targetRefreshToken, requestRefreshToken);


        if(!targetRefreshToken.equals(requestRefreshToken) || !validToken(requestRefreshToken)) { // 리프레시 토큰이 다르거나 유효하지 않은 토큰일경우
            throw new IllegalArgumentException(JwtTokenConstants.INVALID_TOKEN); // 재발급 실패시 로그아웃 시켜야함
        }
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException(MemberConstants.NOT_EXISTS_MEMBER));

        return createAccessToken(member);
    }

}
