package summer.mrplaylist.member.dto;

import lombok.Data;

@Data
public class UpdateMemberRequestDto {
	private Long id;
	private String email;
	private String password;
	private String nickname;
	private String profileImg;
}
