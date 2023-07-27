package summer.mrplaylist.member.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import summer.mrplaylist.member.dto.UpdateMemberRequestDto;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.member.repository.MemberRepository;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public Long join(Member member) {
        if(memberRepository.existsByEmail(member.getEmail())) {
            throw new IllegalStateException("이미 존재하는 이메일입니다."); // 이후 같은 부분 생길 시
        }
        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword())); // 암호화
        memberRepository.save(member);

        return member.getId();
    }

    public Member update(Long id, UpdateMemberRequestDto updateMemberRequestDto) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        member.update(updateMemberRequestDto);
        return member;
    }


}
