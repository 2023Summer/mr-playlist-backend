package summer.mrplaylist.music.model;

import jakarta.persistence.*;
import lombok.*;
import summer.mrplaylist.music.dto.ArtistForm;

import java.util.ArrayList;

@Entity
@Getter
@DiscriminatorValue("A")
public class SoloArtist extends MainArtist {

	// 그룹
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_artist_id")
	private Group group;

	@Builder
	public SoloArtist(Long id, String name, String description, Group group) {
		super(id, name, description, new ArrayList<>());

		this.group = group;
	}

	public static SoloArtist createArtist(ArtistForm artistForm) {
		SoloArtist soloArtist = SoloArtist.builder()
			.name(artistForm.getName())
			.description(artistForm.getDescription())
			.build();
		return soloArtist;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
}
