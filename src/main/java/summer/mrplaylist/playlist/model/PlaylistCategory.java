package summer.mrplaylist.playlist.model;

import jakarta.persistence.*;
import summer.mrplaylist.music.model.Music;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PlaylistCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
