package summer.mrplaylist.member.dto;

import lombok.Data;

@Data
public class AddMemberRequestDto {
	private String email;
	private String password;
	private String nickname;
}
