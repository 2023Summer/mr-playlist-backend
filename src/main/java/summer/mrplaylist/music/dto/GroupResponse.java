package summer.mrplaylist.music.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType;
import summer.mrplaylist.music.model.Group;

@Getter
@Builder
@AllArgsConstructor
public class GroupResponse {

	private String type;
	private Long groupId;
	private String groupName;
	private String description;
	private List<ArtistDto> artistDtoList;
	private List<MusicDto> musicList;

	public static GroupResponse toResponse(Group group) {
		return GroupResponse.builder()
			.type("Group")
			.groupId(group.getId())
			.groupName(group.getName())
			.description(group.getDescription())
			.artistDtoList(
				group.getGroupSoloArtistList().stream()
					.map(soloArtist -> new ArtistDto(soloArtist.getId(), soloArtist.getName(), soloArtist.getImgUrl()))
					.toList()
			)
			.musicList(group.getMusicList().stream().map(m ->
				new MusicDto(m.getId(), m.getName(), m.getImgUrl())
			).limit(4).toList())
			.build();
	}

	@Data
	@AllArgsConstructor
	static class MusicDto {
		private Long musicId;
		private String musicName;
		private String musicImgUrl;
	}

	@Data
	@AllArgsConstructor
	static class ArtistDto {
		private Long artistId;
		private String artistName;
		private String artistImgUrl;
	}
}
