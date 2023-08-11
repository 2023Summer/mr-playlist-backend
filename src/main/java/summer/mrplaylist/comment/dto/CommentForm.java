package summer.mrplaylist.comment.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import summer.mrplaylist.member.model.Member;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class CommentForm {
	private Long playlistId;
	private Member member;
	private String content;
}