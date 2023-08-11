package summer.mrplaylist.music.dto;

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
public class MusicForm {
	private String name;
	private String url;
	private String description;

	private GroupForm groupForm;
	private List<ArtistForm> artistFormList;
}
