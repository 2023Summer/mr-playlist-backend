package summer.mrplaylist.playlist.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.music.dto.MusicForm;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class PlaylistForm {
	private String plName;
	private String plDescription;
	private Member member;
	private List<String> categoryNameList;
	private List<MusicForm> musicFormList;
}
