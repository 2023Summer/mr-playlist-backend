package summer.mrplaylist.music.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.playlist.model.Playlist;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Music {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "music_id")
	private Long id;

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@Column(name = "url", length = 512, nullable = false)
	private String url;

	@Column(name = "description", length = 200)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "artist_id")
	private MainArtist artist;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "playlist_id")
	private Playlist playlist;

	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	public static Music createMusic(MusicForm musicForm, MainArtist mainArtist) {
		Music music = Music.builder()
			.name(musicForm.getName())
			.url(musicForm.getUrl())
			.description(musicForm.getDescription())
			.build();
		music.addArtist(mainArtist);
		return music;
	}

	private void addArtist(MainArtist mainArtist) {
		this.artist = mainArtist;
		mainArtist.addMusic(this);
	}

	public void addPlaylist(Playlist playlist) {
		this.playlist = playlist;
		playlist.addMusic(this);
	}

}
