package summer.mrplaylist.playlist.service;

import static org.assertj.core.api.Assertions.*;
import static summer.mrplaylist.CreateMethod.*;

import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.member.repository.MemberRepository;
import summer.mrplaylist.music.model.Music;
import summer.mrplaylist.playlist.dto.PlaylistForm;
import summer.mrplaylist.playlist.dto.PlaylistResponse;
import summer.mrplaylist.playlist.model.Playlist;

@Slf4j
@Transactional
@SpringBootTest
@DisplayName("플레이 리스트 생성 통합 테스트")
public class PlaylistServiceTest {

	@Autowired
	PlaylistService playlistService;

	@Autowired
	MemberRepository memberRepository;

	@Test
	@DisplayName("플레이리스트 생성")
	public void createPlaylist() throws Exception {
		//given
		Member member = getMember();
		member = memberRepository.save(member);

		PlaylistForm playlistForm = getPlaylistForm(member);

		playlistForm.setMusicFormList(getMusicFormList());
		//when

		Playlist playlist = playlistService.create(playlistForm);
		//then

		assertThat(playlist.getName()).isEqualTo(playlistForm.getPlName());
		assertThat(playlist.getMusicCount()).isEqualTo(1);
		assertThat(playlist.getMember()).isEqualTo(member);
		assertThat(playlist.getMusicList().stream().map(Music::getName)).contains("역몽");

		log.info("플레이리스트에 속한 대표 가수: {}",
			playlist.getMusicList().stream().map((m -> m.getArtist().getName())).collect(Collectors.toList()));
	}

	@DisplayName("플레이리스트 조회")
	@Test
	public void getPlaylistInfo() throws Exception {
		//given
		Member member = getMember();
		member = memberRepository.save(member);

		PlaylistForm playlistForm = getPlaylistForm(member);

		playlistForm.setMusicFormList(getMusicFormList());
		Playlist playlist = playlistService.create(playlistForm);
		//when
		PlaylistResponse playlistInfo = playlistService.findPlaylistInfo(playlist.getId());
		//then
		assertThat(playlistInfo.getCommentCount()).isEqualTo(0);
		assertThat(playlistInfo.getDescription()).isEqualTo(playlist.getDescription());
		assertThat(playlistInfo.getMemberId()).isEqualTo(member.getId());

	}
}
