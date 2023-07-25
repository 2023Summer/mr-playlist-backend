package summer.mrplaylist.music.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.playlist.model.Playlist;

import java.time.LocalDateTime;

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
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public static Music createMusic(MusicForm musicForm, Artist artist){
        Music music = Music.builder()
                .name(musicForm.getName())
                .url(musicForm.getUrl())
                .description(musicForm.getDescription())
                .build();
        music.addArtist(artist);
        return music;
    }

    private void addArtist(Artist artist) {
        this.artist = artist;
        artist.addMusic(this);
    }

}
