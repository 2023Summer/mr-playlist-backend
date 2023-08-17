package summer.mrplaylist.likes.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

	private final LikeRedisService likeRedisService;
	private static final String PLAYLIST_LIKES_KEY = "likes:playlist:";

	public void playlistAddLike(Long playlistId, Long memberId) {
		String key = PLAYLIST_LIKES_KEY + playlistId.toString();
		log.info(key);
		likeRedisService.setData(key, memberId.toString());
	}

	public void playlistDeleteLike(Long playlistId, Long memberId) {
		likeRedisService.deleteData(PLAYLIST_LIKES_KEY + playlistId.toString(), memberId.toString());
	}

}
