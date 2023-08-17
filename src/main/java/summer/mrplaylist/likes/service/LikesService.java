package summer.mrplaylist.likes.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.likes.model.Likes;
import summer.mrplaylist.likes.repository.LikesRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikesService {

	private final LikesRedisService likesRedisService;

	private final LikesRepository likesRepository;
	private static final String PLAYLIST_LIKES_KEY = "likes:playlist:";

	public void playlistAddLike(Long playlistId, Long memberId) {
		String key = PLAYLIST_LIKES_KEY + playlistId.toString();
		log.info(key);
		likesRedisService.setData(key, memberId);
	}

	public void playlistDeleteLike(Long playlistId, Long memberId) {
		likesRedisService.deleteData(PLAYLIST_LIKES_KEY + playlistId.toString(), memberId);
	}

	public void playlistLikeSave(Likes likes) {
		likesRepository.save(likes);
	}

}
