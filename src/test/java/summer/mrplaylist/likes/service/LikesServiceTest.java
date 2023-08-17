package summer.mrplaylist.likes.service;

import static summer.mrplaylist.CreateMethod.*;

import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.likes.dto.LikesForm;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.member.repository.MemberRepository;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.playlist.dto.PlaylistForm;
import summer.mrplaylist.playlist.model.Playlist;
import summer.mrplaylist.playlist.service.PlaylistService;

@SpringBootTest
@Slf4j
class LikesServiceTest {

	@Autowired
	LikesService likesService;

	@Autowired
	PlaylistService playlistService;

	@Autowired
	LikesRedisService likesRedisService;

	@Autowired
	MemberRepository memberRepository;

	@Test
	void playlistAddLike() {
		// given
		Member member = getMember();
		member = memberRepository.save(member);

		PlaylistForm playlistForm = getPlaylistForm(member);
		List<MusicForm> musicFormList = getMusicFormList();
		Playlist playlist = playlistService.create(playlistForm, musicFormList);

		// when
		likesService.playlistAddLike(new LikesForm(playlist.getId(), member.getId()));
		Set<Long> likes = likesRedisService.getAllData("likes:playlist:" + playlist.getId().toString());
		// then
		Assertions.assertThat(member.getId()).isIn(likes);
		Assertions.assertThat(1).isEqualTo(likes.size());

	}

	@Test
	void playlistDeleteLike() {
		//given
		Member member = getMember();
		member = memberRepository.save(member);

		PlaylistForm playlistForm = getPlaylistForm(member);
		List<MusicForm> musicFormList = getMusicFormList();
		Playlist playlist = playlistService.create(playlistForm, musicFormList);

		// when
		likesService.playlistAddLike(new LikesForm(playlist.getId(), member.getId()));
		likesService.playlistDeleteLike(playlist.getId(), member.getId());
		Set<Long> likes = likesRedisService.getAllData("likes:playlist:" + playlist.getId().toString());
		// then
		Assertions.assertThat(member.getId().toString()).isNotIn(likes);
		Assertions.assertThat(0).isEqualTo(likes.size());
	}

	@Test
	void 좋아요_여러명_테스트() {
		// given
		Long playlistId = 1L;
		// when
		for (int memberId = 0; memberId < 10; memberId++) {
			likesService.playlistAddLike(new LikesForm(playlistId, Long.valueOf(memberId)));
		}
		Set<Long> likes = likesRedisService.getAllData("likes:playlist:" + playlistId);
		// then
		Assertions.assertThat(10).isEqualTo(likes.size());
	}
}
