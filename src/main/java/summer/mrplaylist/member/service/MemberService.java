package summer.mrplaylist.member.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.common.service.JwtTokenProvider;
import summer.mrplaylist.common.service.RedisService;
import summer.mrplaylist.member.constant.MemberConstants;
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

	public Member join(Member member) {
		if (memberRepository.existsByEmail(member.getEmail())) {
			throw new IllegalStateException("이미 존재하는 이메일입니다."); // 이후 같은 부분 생길 시
		}
		member.setPassword(bCryptPasswordEncoder.encode(member.getPassword())); // 암호화
		memberRepository.save(member);

		return member;
	}

	public Member update(UpdateMemberRequestDto requestDto) {
		Member member = findMember(requestDto.getId());
		member.updateMember(requestDto);
		return member;
	}

	public Member findMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException(MemberConstants.NOT_EXISTS_MEMBER));
		return member;
	}
}
