package summer.mrplaylist.music.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_id")
    private Long id;
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    @Column(name = "description", length = 200)
    private String description;
    @Column(name = "total_artist", nullable = false)
    private Integer totalArtist;
    // 순환참조
    // 그룹
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_artist_id")
    private Artist groupArtist;
    // 그룹 인원
    @OneToMany(mappedBy = "groupArtist", cascade = CascadeType.PERSIST)
    private List<Artist> groupArtistList = new ArrayList<Artist>();

    @OneToMany(mappedBy = "artist")
    private List<Music> musicList = new ArrayList<Music>();
}
