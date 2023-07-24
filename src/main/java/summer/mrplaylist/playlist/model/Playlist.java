package summer.mrplaylist.playlist.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import summer.mrplaylist.member.model.Member;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @Column(name = "music_count", nullable = false)
    private Integer musicCount;
    @Column(name = "url", length = 100, nullable = false)
    private String name;
    @Column(name = "views", nullable = false)
    @ColumnDefault("0")
    private Integer views;
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "playlist")
    private List<PlaylistCategory> categoryList = new ArrayList<>();
    @OneToMany(mappedBy = "playlist")
    private List<PlaylistMusic> musicList = new ArrayList<>();



}
