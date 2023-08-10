package summer.mrplaylist.common.dto;

import lombok.*;
import summer.mrplaylist.member.constant.OAuthProvider;
import summer.mrplaylist.member.constant.Role;
import summer.mrplaylist.member.model.Member;

import java.lang.annotation.ElementType;
import java.util.Map;


@Getter
public class OAuth2Attributes {
	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String name;
	private String email;
	private String profileImg;

	@Builder
	public OAuth2Attributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String profileImg) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.name = name;
		this.email = email;
		this.profileImg = profileImg;
	}

	public static OAuth2Attributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
		// registrationId 를 통해 후에 여러 서비스 분리
		OAuthProvider oAuthProvider = OAuthProvider.valueOf(registrationId.toUpperCase());
		switch (oAuthProvider) {
			case GOOGLE:
				return ofGoogle(userNameAttributeName, attributes);
//			case KAKAO:
//				return null;
			default:
				throw new IllegalArgumentException("제공자를 찾을 수 없습니다.");
		}
	}

	private static OAuth2Attributes ofGoogle(String nameAttributeKey, Map<String, Object> attributes) {
		return OAuth2Attributes.builder()
			.attributes(attributes)
			.nameAttributeKey(nameAttributeKey)
			.name((String) attributes.get("name"))
			.email((String) attributes.get("email"))
			.profileImg((String) attributes.get("picture"))
			.build();
	}

	public Member toEntity() {
		return Member.builder()
			.email(email)
			.password("NOT_PASS")
			.nickname(name)
			.profileImg(profileImg)
			.role(Role.USER)
			.build();
	}
}
