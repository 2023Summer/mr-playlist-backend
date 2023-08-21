package summer.mrplaylist.playlist.service;

import static org.assertj.core.api.Assertions.*;
import static summer.mrplaylist.CreateMethod.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.member.repository.MemberRepository;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.music.model.Music;
import summer.mrplaylist.music.repository.MusicRepository;
import summer.mrplaylist.playlist.dto.PlaylistForm;
import summer.mrplaylist.playlist.model.Playlist;

@Slf4j
@Transactional
@SpringBootTest
@DisplayName("플레이 리스트 생성 통합 테스트")
public class PlaylistServiceSpringBootTest {

	@Autowired
	PlaylistService playlistService;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	MusicRepository musicRepository;

	@Test
	@DisplayName("플레이리스트 생성")
	public void createPlaylist() throws Exception {
		//given
		Member member = getMember();
		member = memberRepository.save(member);

		List<String> categoryNameList = getCategoryNameList();

		List<MusicForm> musicFormList = new ArrayList<>();
		musicFormList.add(getMusicForm("킹 누", "일본 5인조", "역몽", "주술회전0 ost"));
		musicFormList.add(getMusicForm("IU", "국힙 원탑", "Coin", "서브곡"));

		PlaylistForm playlistForm = PlaylistForm.builder()
			.plName("킹 누의 명곡")
			.plDescription("노동요")
			.categoryNameList(categoryNameList)
			.musicFormList(musicFormList)
			.member(member)
			.build();
		//when

		Playlist playlist = playlistService.create(playlistForm);
		//then

		assertThat(playlist.getName()).isEqualTo(playlistForm.getPlName());
		assertThat(playlist.getMusicCount()).isEqualTo(2);
		assertThat(playlist.getMember()).isEqualTo(member);
		assertThat(playlist.getMusicList().stream().map(Music::getName)).contains("역몽");

		log.info("플레이리스트에 속한 대표 가수: {}",
			playlist.getMusicList().stream().map((m -> m.getArtist().getName())).collect(Collectors.toList()));

	}

	@Test
	@DisplayName("플레이리스트 음악 삭제")
	public void deleteTest() throws Exception {
		//given
		Member member = getMember();
		member = memberRepository.save(member);

		List<String> categoryNameList = getCategoryNameList();

		PlaylistForm playlistForm = PlaylistForm.builder()
			.plName("킹 누의 명곡")
			.plDescription("노동요")
			.categoryNameList(categoryNameList)
			.member(member)
			.build();

		List<MusicForm> musicFormList = new ArrayList<>();
		MusicForm musicForm = getMusicForm("킹 누", "일본 5인조", "test-역몽", "주술회전0 ost");
		musicFormList.add(musicForm);
		musicFormList.add(getMusicForm("빅뱅", "YG 댄스가수", "붉은 노을", "서브곡"));
		playlistForm.setMusicFormList(musicFormList);

		Playlist playlist = playlistService.create(playlistForm);
		Music music = musicRepository.findByName(musicForm.getName()).get();
		//when
		playlistService.deleteMusic(playlist.getId(), music.getId());
		//then

		assertThat(playlist.getMusicCount()).isEqualTo(1);
		assertThat(playlist.getMusicList().stream().map(m -> m.getName()).toList()).doesNotContain(musicForm.getName());
	}

	@Test
	@DisplayName("하나의 음악만 있는 플레이리스트 삭제")
	public void deleteOneMusicTest() throws Exception {
		//given
		PlaylistForm playlistForm = getPlaylistForm();

		List<MusicForm> musicFormList = new ArrayList<>();
		MusicForm musicForm = getMusicForm("킹 누", "일본 5인조", "역몽", "주술회전0 ost");
		musicFormList.add(musicForm);
		playlistForm.setMusicFormList(musicFormList);

		Playlist playlist = playlistService.create(playlistForm);
		Music music = musicRepository.findByName(musicForm.getName()).get();

		assertThatThrownBy(() -> playlistService.deleteMusic(playlist.getId(), music.getId()))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("플레이리스트는 촤소 1개의 음악을 가져야 합니다.");
	}

	private PlaylistForm getPlaylistForm() {
		Member member = getMember();
		member = memberRepository.save(member);

		List<String> categoryNameList = getCategoryNameList();

		PlaylistForm playlistForm = PlaylistForm.builder()
			.plName("킹 누의 명곡")
			.plDescription("노동요")
			.categoryNameList(categoryNameList)
			.member(member)
			.build();
		return playlistForm;
	}

}
