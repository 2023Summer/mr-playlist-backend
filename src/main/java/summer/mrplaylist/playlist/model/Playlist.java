package summer.mrplaylist.playlist.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.music.model.Music;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@EntityListeners(AuditingEntityListener.class)
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

    @Column(name = "url", length = 100, nullable = false)
    private String name;
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;


}
