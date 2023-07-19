package summer.mrplaylist.comment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.playlist.model.Playlist;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @NotNull
    private String content;




}
