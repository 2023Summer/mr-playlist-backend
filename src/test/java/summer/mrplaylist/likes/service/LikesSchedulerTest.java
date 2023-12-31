package summer.mrplaylist.likes.service;

import static summer.mrplaylist.CreateMethod.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import summer.mrplaylist.likes.constant.LikesConstants;
import summer.mrplaylist.likes.dto.LikesForm;
import summer.mrplaylist.likes.model.Likes;
import summer.mrplaylist.likes.repository.LikesQRepo;
import summer.mrplaylist.likes.repository.LikesRepository;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.member.repository.MemberRepository;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.playlist.dto.PlaylistForm;
import summer.mrplaylist.playlist.model.Playlist;
import summer.mrplaylist.playlist.service.PlaylistService;

@SpringBootTest
@Transactional
class LikesSchedulerTest {
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	PlaylistService playlistService;
	@Autowired
	LikesScheduler likesScheduler;
	@Autowired
	LikesRepository likesRepository;
	@Autowired
	LikesService likesService;
	@Autowired
	LikesRedisService likesRedisService;
	@Autowired
	LikesQRepo likesQRepo;

	@Test
	void updatePlaylistLikes() throws Exception {
		// given
		likesRedisService.flushAll();
		Member member = getMember();
		member = memberRepository.save(member);

		PlaylistForm playlistForm = getPlaylistForm(member);
		List<MusicForm> musicFormList = getMusicFormList();
		Playlist playlist = playlistService.create(playlistForm, musicFormList);

		// when
		// member 좋아요 Redis에 저장
		likesService.playlistAddLike(new LikesForm(playlist.getId(), member.getId()));

		likesScheduler.updatePlaylistLikes();
		Likes likes1 = likesRepository.findByPlaylistIdAndMemberId(playlist.getId(),
			member.getId()).orElseThrow(() -> new IllegalStateException("not found likes"));
		List<Likes> likesList = likesQRepo.findByPlaylistId(playlist.getId());

		//then
		// 검증 및 saved 전환 확인
		Assertions.assertThat(member).isEqualTo(likes1.getMember());
		Assertions.assertThat(
				likesRedisService.existData(LikesConstants.SAVED_LIKES_PREFIX + playlist.getId(), member.getId()))
			.isTrue();
	}

	@Test
	void 삭제_테스트() throws Exception {
		// given
		likesRedisService.flushAll();
		Member member = getMember();
		Member member2 = getMember2();
		member = memberRepository.save(member);
		member2 = memberRepository.save(member2);

		PlaylistForm playlistForm = getPlaylistForm(member);
		List<MusicForm> musicFormList = getMusicFormList();
		Playlist playlist = playlistService.create(playlistForm, musicFormList);

		// when
		// member2 좋아요 제거
		likesRepository.save(Likes.createLikes(playlist, member2));
		likesService.playlistDeleteLike(playlist.getId(), member2.getId());

		likesScheduler.updatePlaylistLikes();
		List<Likes> likesList = likesQRepo.findByPlaylistId(playlist.getId());

		//then
		Assertions.assertThat(likesQRepo.existsByPlaylistIdAndMemberId(playlist.getId(), member2.getId())).isFalse();
	}
}
