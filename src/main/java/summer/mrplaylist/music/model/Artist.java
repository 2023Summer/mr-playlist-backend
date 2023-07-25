package summer.mrplaylist.music.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    private List<Artist> groupArtistList = new ArrayList<Artist>();

    @Builder.Default
    @OneToMany(mappedBy = "artist")
    private List<Music> musicList = new ArrayList<Music>();

    public void addMusic(Music music) {
        this.musicList.add(music);
    }
}
