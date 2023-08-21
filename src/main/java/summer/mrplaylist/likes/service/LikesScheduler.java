package summer.mrplaylist.likes.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.likes.constant.LikesConstants;
import summer.mrplaylist.likes.model.Likes;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.member.service.MemberService;
import summer.mrplaylist.playlist.model.Playlist;
import summer.mrplaylist.playlist.service.PlaylistService;

@Component
@RequiredArgsConstructor
@Slf4j
public class LikesScheduler {
	private final LikesService likesService;
	private final LikesRedisService likesRedisService;
	private final MemberService memberService;
	private final PlaylistService playlistService;

	@Scheduled(fixedDelay = 600000) // 10분마다 실행
	public void updatePlaylistLikes() {
		Set<String> keys = getLikesKeys();
		if (keys.isEmpty()) {
			throw new IllegalStateException(LikesConstants.NOT_FOUND);
		}
		for (String key : keys) {
			Set<Long> memberIdSet = likesRedisService.getAllData(key);
			for (Long memberId : memberIdSet) {
				Long playlistId = Long.parseLong(key.split(":")[2]);
				// 키의 상태가 add, delete 일때 구분
				if (key.startsWith(LikesConstants.ADD_LIKES_PREFIX)) {
					saveData(playlistId, memberId);
					renameKey(key); // 저장 후 키 상태 saved 변경
				} else if (key.startsWith(LikesConstants.DELETE_LIKES_PREFIX)) {
					likesService.playlistLikeDeleteDB(playlistId, memberId);
				}
			}
		}
	}

	private void saveData(Long playlistId, Long memberId) {
		Member member = memberService.findMember(memberId);
		Playlist playlist = playlistService.findPlaylist(playlistId);
		Likes likes = Likes.createLikes(playlist, member);
		likesService.playlistLikeSaveDB(likes);
	}

	private void renameKey(String oldKey) {
		String newKey = LikesConstants.SAVED_LIKES_PREFIX + oldKey.split(":")[2];

		likesRedisService.renameKey(oldKey, newKey);
	}

	private Set<String> getLikesKeys() {
		Set<String> keys = new HashSet<>();
		keys.addAll(likesRedisService.getKeys(LikesConstants.ADD_LIKES_PREFIX + "*"));
		keys.addAll(likesRedisService.getKeys(LikesConstants.DELETE_LIKES_PREFIX + "*"));
		for (String key : keys) {
			System.out.println("key = " + key);
		}
		return keys;
	}

}
