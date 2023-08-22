package summer.mrplaylist.playlist.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.ToString;
import summer.mrplaylist.comment.dto.CommentResponse;
import summer.mrplaylist.music.dto.MusicSimpleResponse;
import summer.mrplaylist.music.model.Music;
import summer.mrplaylist.playlist.model.Playlist;

/**
 * 플리 상세 페이지 정보 확인
 */
@Getter
@ToString
public class PlaylistResponse {

	private Long id;
	private String name;
	private Long memberId;
	private String memberName;
	private String description;
	private Integer musicCount;
	private Integer commentCount;
	private int views;
	private LocalDateTime createdAt;

	private List<MusicSimpleResponse> musicList;
	private List<CommentResponse> commentList;

	public PlaylistResponse(Playlist playlist, List<Music> musicList) {
		this.id = playlist.getId();
		this.name = playlist.getName();
		this.memberId = playlist.getMember().getId();
		this.memberName = playlist.getMember().getNickname();
		this.description = playlist.getDescription();
		this.musicCount = playlist.getMusicCount();
		this.commentCount = playlist.getCommentCount();
		this.views = playlist.getViews();
		this.createdAt = playlist.getCreatedAt();
		this.musicList = musicList.stream()
			.map(MusicSimpleResponse::new)
			.toList();
		this.commentList = playlist.getCommentList().stream()
			.map(CommentResponse::new)
			.toList();
	}
}
