package summer.mrplaylist.playlist.model;

import jakarta.persistence.*;
import summer.mrplaylist.music.model.Music;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PlaylistMusic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_music_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;
    @OneToMany(mappedBy = "playlist_music")
    private List<Music> musicList = new ArrayList<Music>();
}
