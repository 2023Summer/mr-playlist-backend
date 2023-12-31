package summer.mrplaylist.common.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.common.dto.OAuth2Attributes;
import summer.mrplaylist.common.model.UserPrincipal;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final MemberRepository memberRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);
		// 현재 서비스 구분 문자열
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();
		log.info("registrationId : {}", registrationId);
		OAuth2Attributes attributes = OAuth2Attributes.of(registrationId, userNameAttributeName,
			oAuth2User.getAttributes());

		// return new DefaultOAuth2User(oAuth2User);
		Member socialMember = saveOrUpdate(attributes, registrationId);
		return new UserPrincipal(socialMember, attributes.getAttributes());
	}

	public Member saveOrUpdate(OAuth2Attributes attributes, String registrationId) {
		Member member = memberRepository.findByEmail(attributes.getEmail())
			.map(entity -> entity.updateSocialMember(attributes))
			.orElse(attributes.toEntity(registrationId));
		return memberRepository.save(member);
	}

}
