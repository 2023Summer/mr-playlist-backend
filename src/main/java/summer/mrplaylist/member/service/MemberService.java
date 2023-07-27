package summer.mrplaylist.member.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import summer.mrplaylist.member.dto.UpdateMemberRequestDto;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.member.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public Long join(Member member) {
        if(memberRepository.existsByEmail(member.getEmail())) {
            throw new IllegalStateException("이미 존재하는 이메일");
        }
        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword())); // 암호화
        memberRepository.save(member);

        return member.getId();
    }

    public Member update(Long id, UpdateMemberRequestDto updateMemberRequestDto) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 id의 회원 없음"));
        member.update(updateMemberRequestDto);
        return member;
    }


}
