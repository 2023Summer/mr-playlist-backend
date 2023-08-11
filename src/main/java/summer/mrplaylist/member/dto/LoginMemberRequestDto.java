package summer.mrplaylist.member.dto;

import lombok.Data;

@Data
public class LoginMemberRequestDto {
	private String email;
	private String password;
}
