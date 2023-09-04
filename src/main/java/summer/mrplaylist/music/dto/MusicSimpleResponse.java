package summer.mrplaylist.music.dto;

import lombok.Getter;
import summer.mrplaylist.music.model.Music;

/**
 * 간단한 음악 반환 정보
 */
@Getter
public class MusicSimpleResponse {

	private Long musicId;
	private String musicName;
	private String musicImgUrl;

	private Long artistId;
	private String artistName;

	public MusicSimpleResponse(Music music) {
		this.musicId = music.getId();
		this.musicName = music.getName();
		this.musicImgUrl = music.getImgUrl();
		this.artistId = music.getArtist().getId();
		this.artistName = music.getArtist().getName();
	}
}
