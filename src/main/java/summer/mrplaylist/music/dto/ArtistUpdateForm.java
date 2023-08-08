package summer.mrplaylist.music.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistUpdateForm {
	private Long id;
	private String name;
	private String description;
}
