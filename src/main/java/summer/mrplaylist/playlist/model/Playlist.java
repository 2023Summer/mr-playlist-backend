package summer.mrplaylist.playlist.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.music.model.Music;
import summer.mrplaylist.playlist.dto.PlaylistForm;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
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

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "views", nullable = false)
    @ColumnDefault("0")
    private Integer views;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "description")
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "playlist")
    private List<PlaylistCategory> categoryList = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "playlist", cascade = CascadeType.PERSIST)
    private List<Music> musicList = new ArrayList<>();

    public static Playlist createPlaylist(PlaylistForm playlistForm){
        return Playlist.builder()
                .name(playlistForm.getPlName())
                .description(playlistForm.getPlDescription())
                .views(0)
                .musicCount(0)
                .member(playlistForm.getMember())
                .build();
    }
    public void addMusic(Music music){
        this.musicList.add(music);
        this.musicCount +=1;
    }

}
