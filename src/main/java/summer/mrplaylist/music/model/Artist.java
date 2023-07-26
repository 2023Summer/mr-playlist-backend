package summer.mrplaylist.music.model;

import jakarta.persistence.*;
import lombok.*;

import summer.mrplaylist.music.dto.ArtistForm;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
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
    @Builder.Default
    @OneToMany(mappedBy = "groupArtist", cascade = CascadeType.PERSIST)
    private List<Artist> groupArtistList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "artist")

    private List<Music> musicList = new ArrayList<>();

    // 생성 메소드
    public static Artist createArtist(ArtistForm artistForm){
        Artist artist = Artist.builder()
                .name(artistForm.getName())
                .description(artistForm.getDescription())
                .totalArtist(0)
                .build();
        return artist;
    }

    public void addArtist(Artist artist){
        artist.groupArtist = this;
        this.groupArtistList.add(artist);
        this.totalArtist += 1;
    }

    public String addDescription(String description){
        this.description= this.getDescription()+", "+ description;
        return description;

    public void addMusic(Music music) {
        this.musicList.add(music);

    }
}
