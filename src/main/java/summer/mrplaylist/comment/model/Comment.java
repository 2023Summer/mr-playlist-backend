package summer.mrplaylist.comment.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import summer.mrplaylist.comment.dto.CommentForm;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.playlist.model.Playlist;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
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

    @Column(name = "content", length = 512, nullable = false)
    private String content;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    public static Comment createComment(CommentForm commentForm, Member member, Playlist playlist){
        Comment comment = Comment.builder()
                .content(commentForm.getContent())
                .member(member)
                .playlist(playlist)
                .build();
        playlist.addComment(comment);
        return comment;
    }


    public void update(String content) {
        this.content = content;
    }




}
