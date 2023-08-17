package summer.mrplaylist.likes.dto;

import lombok.Data;

@Data
public class LikesForm {
	private Long playlistId;
	private Long memberId;
}
