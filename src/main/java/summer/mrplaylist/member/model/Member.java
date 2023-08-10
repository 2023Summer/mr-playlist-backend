package summer.mrplaylist.member.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import summer.mrplaylist.common.dto.OAuth2Attributes;
import summer.mrplaylist.member.constant.OAuthProvider;
import summer.mrplaylist.member.constant.Role;
import summer.mrplaylist.member.dto.AddMemberRequestDto;
import summer.mrplaylist.member.dto.UpdateMemberRequestDto;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(name = "email", nullable = false, length = 100)
	private String email;

	@Column(name = "password", nullable = false, length = 60) // bcrypt 인코딩시 리턴되는 문자열 길이는 60
	private String password;

	@Column(name = "nickname", nullable = false, length = 50)
	private String nickname;

	@Column(name = "profile_img", length = 512)
	private String profileImg;

	@Enumerated(EnumType.STRING)
	@Column(name = "oauth_provider")
	private OAuthProvider oAuthProvider;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role;

	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "modified_at")
	private LocalDateTime modifiedAt;

	@Builder
	public Member(String email, String password, String nickname, String profileImg, Role role) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.profileImg = profileImg;
		this.role = role;
	}

	public static Member createMember(AddMemberRequestDto requestDto) {
		return Member.builder()
			.email(requestDto.getEmail())
			.password(requestDto.getPassword())
			.nickname(requestDto.getNickname())
			.role(Role.USER)
			.build();
	}

	public void update(UpdateMemberRequestDto requestDto) {
		this.email = requestDto.getEmail();
		this.password = requestDto.getPassword();
		this.nickname = requestDto.getNickname();
		this.profileImg = requestDto.getProfileImg();
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
