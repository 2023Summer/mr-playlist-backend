package summer.mrplaylist.common.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import summer.mrplaylist.member.constant.OAuthProvider;
import summer.mrplaylist.member.constant.Role;
import summer.mrplaylist.member.model.Member;

@Getter
public class OAuth2Attributes {
	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String name;
	private String email;
	private String profileImg;

	@Builder
	public OAuth2Attributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email,
		String profileImg) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.name = name;
		this.email = email;
		this.profileImg = profileImg;
	}

	public static OAuth2Attributes of(String registrationId, String userNameAttributeName,
		Map<String, Object> attributes) {
		// registrationId 를 통해 후에 여러 서비스 분리
		OAuthProvider oAuthProvider = OAuthProvider.valueOf(registrationId.toUpperCase());
		return switch (oAuthProvider) {
			case GOOGLE -> ofGoogle(userNameAttributeName, attributes);
			case KAKAO -> ofKakao(userNameAttributeName, attributes);
			default -> throw new IllegalArgumentException("제공자를 찾을 수 없습니다.");
		};
	}

	private static OAuth2Attributes ofGoogle(String nameAttributeKey, Map<String, Object> attributes) {
		return OAuth2Attributes.builder()
			.attributes(attributes)
			.nameAttributeKey(nameAttributeKey)
			.name((String)attributes.get("name"))
			.email((String)attributes.get("email"))
			.profileImg((String)attributes.get("picture"))
			.build();
	}

	private static OAuth2Attributes ofKakao(String nameAttributeKey, Map<String, Object> attributes) {
		// 카카오는 attributes -> kakao_account(email) -> profile(nickname, profile_image)
		Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
		Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

		return OAuth2Attributes.builder()
			.attributes(attributes)
			.nameAttributeKey(nameAttributeKey)
			.name((String)kakaoProfile.get("nickname"))
			.email((String)kakaoAccount.get("email"))
			.profileImg((String)kakaoProfile.get("profile_image_url"))
			.build();
	}

	public Member toEntity(String registrationId) {
		return Member.builder()
			.email(email)
			.password("NOT_PASS")
			.nickname(name)
			.profileImg(profileImg)
			.role(Role.USER)
			.oAuthProvider(OAuthProvider.valueOf(registrationId.toUpperCase()))
			.build();
	}
}
