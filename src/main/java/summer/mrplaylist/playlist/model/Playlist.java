package summer.mrplaylist.playlist.model;

import jakarta.persistence.*;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.music.model.Music;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id")
    private Long id;
    @OneToMany(mappedBy = "music")
    private List<Music> musicList = new ArrayList<Music>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private Integer musicCount;
    private String name;
}
