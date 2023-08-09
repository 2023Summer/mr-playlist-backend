package summer.mrplaylist.playlist.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class PlaylistUpdateForm {

	private Long plId;
	private String plName;
	private String plDescription;

	private List<Long> musicList;
}
