package summer.mrplaylist.playlist.dto;

import lombok.Getter;
import summer.mrplaylist.playlist.model.Playlist;

/**
 * 메인 화면에서 보여줄
 * 간단한 정보만 들어있는 반환 Dto
 */
@Getter
public class PlaylistSimpleResponse {
	private Long id;
	private String name;
	private String createdAt;
	private int commentCount;

	public PlaylistSimpleResponse(Playlist playlist) {
		this.id = playlist.getId();
		this.name = playlist.getName();
		this.createdAt = playlist.getCreatedAt().toString();
		this.commentCount = playlist.getCommentCount();
	}
}
