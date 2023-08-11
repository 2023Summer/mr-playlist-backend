package summer.mrplaylist.music.model;

import jakarta.persistence.*;
import lombok.*;

import summer.mrplaylist.music.dto.ArtistForm;

import java.util.ArrayList;
import java.util.List;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@AllArgsConstructor
@Getter
@Entity
public abstract class MainArtist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "artist_id")
	private Long id;
	@Column(name = "name", length = 100, nullable = false)
	private String name;
	@Column(name = "description", length = 200)
	private String description;
	// 순환참조
	@OneToMany(mappedBy = "artist")
	private List<Music> musicList;

	protected MainArtist() {
	}

	public String addDescription(String description) {
		this.description = this.getDescription() + ", " + description;
		return description;
	}

	public void addMusic(Music music) {
		this.musicList.add(music);
	}
}
