package summer.mrplaylist.common.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import summer.mrplaylist.common.model.UserPrincipal;
import summer.mrplaylist.member.constant.MemberConstants;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.member.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member findMember = memberRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException(MemberConstants.NOT_EXISTS_MEMBER));
		return new UserPrincipal(findMember);
	}
}
