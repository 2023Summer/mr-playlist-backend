package summer.mrplaylist.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import summer.mrplaylist.member.model.Member;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponse {
	private Long id;
	private String email;
	private String nickname;
	private String profileImg;

	public static MemberResponse toResponse(Member member) {
		return MemberResponse.builder()
			.id(member.getId())
			.email(member.getEmail())
			.nickname(member.getNickname())
			.profileImg(member.getProfileImg())
			.build();
	}
}
