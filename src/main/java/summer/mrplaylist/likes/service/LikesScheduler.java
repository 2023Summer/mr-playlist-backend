package summer.mrplaylist.likes.service;

import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
	public void updatePlaylistLikes() throws Exception {
		Set<String> keys = likesRedisService.getKeys("*");
		if (keys.isEmpty()) {
			throw new IllegalStateException("좋아요가 존재하지 않습니다.");
		}
		for (String key : keys) {
			Set<Long> memberIdSet = likesRedisService.getAllData(key);
			for (Long memberId : memberIdSet) {
				Member member = memberService.findMember(memberId);
				Long playlistId = Long.parseLong(key.split(":")[2]);
				Playlist playlist = playlistService.findPlaylist(playlistId);

				Likes likes = Likes.createLikes(member, playlist);
				likesService.playlistLikeSave(likes);
			}
		}
		likesRedisService.flushAll(); // 차후 데이터 백업으로 변경
	}

}
