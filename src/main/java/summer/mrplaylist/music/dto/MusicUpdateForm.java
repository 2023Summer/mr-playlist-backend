package summer.mrplaylist.music.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class MusicUpdateForm {

	private Long musicId;
	private String name;
	private String url;
	private String description;
	private Long mainArtistId;
}
