package summer.mrplaylist.login.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import summer.mrplaylist.common.dto.JwtTokenDto;
import summer.mrplaylist.common.service.JwtTokenProvider;
import summer.mrplaylist.login.dto.LoginMemberRequestDto;
import summer.mrplaylist.member.constant.MemberConstants;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Transactional(readOnly = true)
	public JwtTokenDto login(LoginMemberRequestDto requestDto) {
		Member member = memberRepository.findByEmail(requestDto.getEmail())
			.orElseThrow(() -> new IllegalArgumentException(MemberConstants.LOGIN_FAILURE));
		if (!bCryptPasswordEncoder.matches(requestDto.getPassword(), member.getPassword()))
			throw new IllegalArgumentException(MemberConstants.LOGIN_FAILURE);

		return jwtTokenProvider.createAllToken(member);
	}

	@Transactional
	public void logout(JwtTokenDto jwtTokenDto, String email) {
		jwtTokenProvider.banAccessToken(jwtTokenDto.getAccessToken(), email);
	}
}
