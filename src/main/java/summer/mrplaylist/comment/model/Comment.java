package summer.mrplaylist.comment.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import summer.mrplaylist.comment.dto.CommentForm;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.playlist.model.Playlist;

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

	public static Comment createComment(CommentForm commentForm, Member member, Playlist playlist) {
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
