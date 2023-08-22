package summer.mrplaylist.comment.dto;

import lombok.Getter;
import summer.mrplaylist.comment.model.Comment;

@Getter
public class CommentResponse {
	private Long id;
	private Long memberId;
	private String memberName;
	private String content;
	private String createdAt;

	public CommentResponse(Comment comment) {
		this.id = comment.getId();
		this.content = comment.getContent();
		this.memberId = comment.getMember().getId();
		this.memberName = comment.getMember().getNickname();
		this.createdAt = comment.getContent();
	}
}
