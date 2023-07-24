package summer.mrplaylist.likes.model;


import jakarta.persistence.*;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.playlist.model.Playlist;

@Entity
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlistId;

}
