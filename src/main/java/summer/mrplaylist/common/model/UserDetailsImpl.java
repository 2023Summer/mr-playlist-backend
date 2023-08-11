package summer.mrplaylist.common.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import summer.mrplaylist.member.constant.Role;
import summer.mrplaylist.member.model.Member;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

	private final Member member;
	private static final String ROLE_PREFIX = "ROLE_";

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(ROLE_PREFIX + member.getRole().toString()));
	}

	public Member getMember() {
		return member;
	}

	@Override
	public String getPassword() {
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		return member.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
