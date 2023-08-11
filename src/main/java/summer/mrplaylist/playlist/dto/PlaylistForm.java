package summer.mrplaylist.playlist.dto;

import lombok.*;
import summer.mrplaylist.member.model.Member;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class PlaylistForm {
	private String plName;
	private String plDescription;
	private Member member;

	private List<String> categoryNameList;
}
