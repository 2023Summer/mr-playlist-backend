package summer.mrplaylist.common.dto;

import lombok.*;
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
	private String picture;

	@Builder
	public void OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.name = name;
		this.email = email;
		this.picture = picture;
	}

	public Member toEntity() {
		return Member.builder()
			.email(email)
			.nickname(name) // 임시 닉네임 ? or 이름
			.role(Role.USER)
			.build();

	}
}
