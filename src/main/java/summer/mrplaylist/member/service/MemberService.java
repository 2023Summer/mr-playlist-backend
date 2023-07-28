package summer.mrplaylist.member.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import summer.mrplaylist.common.config.jwt.JwtTokenProvider;
import summer.mrplaylist.common.constant.JwtTokenConstants;
import summer.mrplaylist.common.dto.JwtTokenDto;
import summer.mrplaylist.common.service.RedisService;
import summer.mrplaylist.member.constant.MemberConstants;
import summer.mrplaylist.member.dto.LoginMemberRequestDto;
import summer.mrplaylist.member.dto.UpdateMemberRequestDto;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.member.repository.MemberRepository;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    public Long join(Member member) {
        if(memberRepository.existsByEmail(member.getEmail())) {
            throw new IllegalStateException("이미 존재하는 이메일입니다."); // 이후 같은 부분 생길 시
        }
        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword())); // 암호화
        memberRepository.save(member);

        return member.getId();
    }

    public Member update(Long id, UpdateMemberRequestDto requestDto) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(MemberConstants.NOT_EXISTS_MEMBER));
        member.update(requestDto);
        return member;
    }

    @Transactional(readOnly = true)
    public JwtTokenDto login(LoginMemberRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(MemberConstants.LOGIN_FAILURE));
        if(!bCryptPasswordEncoder.matches(requestDto.getPassword(), member.getPassword()))
            throw new IllegalArgumentException(MemberConstants.LOGIN_FAILURE);

        return jwtTokenProvider.createAllToken(member);
    }

    public String reissueAccessToken(String requestAccessToken, String requestRefreshToken) {

        String userEmail = jwtTokenProvider.getAuthentication(requestAccessToken).getName();

        String targetRefreshToken = redisService.getData(userEmail); // 회원 이메일로 저장된 토큰과 값 비교

        if(!targetRefreshToken.equals(requestRefreshToken) || !jwtTokenProvider.validToken(requestRefreshToken)) { // 리프레시 토큰이 다르거나 유효하지 않은 토큰일경우
            throw new IllegalArgumentException(JwtTokenConstants.INVALID_TOKEN); // 재발급 실패시 로그아웃 시켜야함
        }
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException(MemberConstants.NOT_EXISTS_MEMBER));

        return jwtTokenProvider.createAccessToken(member);
    }




}
