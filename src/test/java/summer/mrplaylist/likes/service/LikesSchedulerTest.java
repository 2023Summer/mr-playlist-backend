package summer.mrplaylist.likes.service;

import static summer.mrplaylist.CreateMethod.*;

import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import summer.mrplaylist.likes.dto.LikesForm;
import summer.mrplaylist.likes.model.Likes;
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

	@Test
	void updatePlaylistLikes() throws Exception {
		// given
		Member member = getMember();
		member = memberRepository.save(member);

		PlaylistForm playlistForm = getPlaylistForm(member);
		List<MusicForm> musicFormList = getMusicFormList();
		Playlist playlist = playlistService.create(playlistForm, musicFormList);

		// when
		likesService.playlistAddLike(new LikesForm(playlist.getId(), member.getId()));
		Set<Long> likes = likesRedisService.getAllData("likes:playlist:" + playlist.getId().toString());

		likesScheduler.updatePlaylistLikes();
		Likes likes1 = likesRepository.findByPlaylistIdAndMemberId(playlist.getId(),
			member.getId()).orElseThrow(() -> new IllegalStateException("not found likes"));
		//then
		Assertions.assertThat(member).isEqualTo(likes1.getMemberId());

	}
}
