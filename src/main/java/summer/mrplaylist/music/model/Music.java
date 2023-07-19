package summer.mrplaylist.music.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "music_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;
    @NotNull
    private String name;
    @NotNull
    private String url;
    private String description;

}
