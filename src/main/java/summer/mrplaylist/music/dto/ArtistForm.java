package summer.mrplaylist.music.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import summer.mrplaylist.music.model.MainArtist;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistForm {

	private String name;
	private String description;
	private String imgUrl;

	public static ArtistForm toDto(MainArtist mainArtist) {
		return new ArtistForm(mainArtist.getName(), mainArtist.getDescription(), mainArtist.getImgUrl());
		//return new ArtistForm(mainArtist.getName(), mainArtist.getDescription());
	}
}
