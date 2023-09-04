package summer.mrplaylist.music.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import summer.mrplaylist.music.model.SoloArtist;

@Getter
@Builder
@AllArgsConstructor
public class SoloArtistResponse {

	private String type;
	private Long artistId;
	private String artistName;
	private String description;
	private String imgUrl;
	private Long groupId;
	private String groupName;
	private List<MusicDto> musicList;

	public static SoloArtistResponse toResponse(SoloArtist soloArtist) {
		return SoloArtistResponse.builder()
			.type("Solo")
			.artistId(soloArtist.getId())
			.description(soloArtist.getDescription())
			.imgUrl(soloArtist.getImgUrl())
			.groupId((soloArtist.getGroup() == null) ? null : soloArtist.getGroup().getId())
			.groupName((soloArtist.getGroup() == null) ? null : soloArtist.getGroup().getName())
			.musicList(soloArtist.getMusicList().stream().map(m ->
				//new MusicDto(m.getId(), m.getName())
				new MusicDto(m.getId(), m.getName(), m.getImgUrl())
			).limit(4).toList())
			.build();
	}

	@Data
	@AllArgsConstructor
	static class MusicDto {
		private Long musicId;
		private String musicName;
		private String musicUrl;
	}
}
