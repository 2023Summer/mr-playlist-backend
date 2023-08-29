package summer.mrplaylist.likes.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.likes.constant.LikesConstants;
import summer.mrplaylist.likes.dto.LikesForm;
import summer.mrplaylist.likes.model.Likes;
import summer.mrplaylist.likes.repository.LikesQRepo;
import summer.mrplaylist.likes.repository.LikesRepository;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LikesService {

	private final LikesRedisService likesRedisService;

	private final LikesRepository likesRepository;

	private final LikesQRepo likesQRepo;

	public void playlistAddLike(LikesForm likesForm) {
		if (likesRedisService.existData(makeKey(LikesConstants.DELETE_LIKES_PREFIX, likesForm.getPlaylistId()),
			likesForm.getMemberId())) {
			likesRedisService.removeData(makeKey(LikesConstants.DELETE_LIKES_PREFIX, likesForm.getPlaylistId()),
				likesForm.getMemberId());
		} else if (!existLikes(likesForm.getPlaylistId(), likesForm.getMemberId())) {
			setLike(LikesConstants.ADD_LIKES_PREFIX, likesForm.getPlaylistId(), likesForm.getMemberId());
		}
	}

	public void playlistDeleteLike(Long playlistId, Long memberId) {
		// Redis 먼저 키가 존재하는지 체크 후, DB에 존재하면 delete 키 셋
		if (likesRedisService.existData(makeKey(LikesConstants.ADD_LIKES_PREFIX, playlistId), memberId)) {
			likesRedisService.removeData(makeKey(LikesConstants.ADD_LIKES_PREFIX, playlistId), memberId);
		} else if (existLikes(playlistId, memberId)) {
			setLike(LikesConstants.DELETE_LIKES_PREFIX, playlistId, memberId);
		} else {
			throw new IllegalStateException(LikesConstants.NOT_FOUND);
		}
	}

	public void playlistLikeSaveDB(Likes likes) {
		likesRepository.save(likes);
	}

	public void playlistLikeDeleteDB(Long playlistId, Long memberId) {
		Likes likes = likesRepository.findByPlaylistIdAndMemberId(playlistId, memberId)
			.orElseThrow(() -> new IllegalStateException(LikesConstants.NOT_FOUND));
		likesRepository.delete(likes);

		likesRedisService.removeData(makeKey(LikesConstants.DELETE_LIKES_PREFIX, playlistId), memberId);
	}

	private boolean existLikes(Long playlistId, Long memberId) {
		return likesQRepo.existsByPlaylistIdAndMemberId(playlistId, memberId);
	}

	private void setLike(String prefix, Long playlistId, Long memberId) {
		likesRedisService.addData(makeKey(prefix, playlistId), memberId);
	}

	private String makeKey(String prefix, Long playlistId) {
		return prefix + playlistId.toString();
	}
}
