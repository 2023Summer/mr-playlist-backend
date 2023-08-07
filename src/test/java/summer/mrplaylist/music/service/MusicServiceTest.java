package summer.mrplaylist.music.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import summer.mrplaylist.music.dto.ArtistForm;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.music.model.Music;
import summer.mrplaylist.music.model.SoloArtist;
import summer.mrplaylist.music.repository.MusicRepository;

@ExtendWith(MockitoExtension.class)
class MusicServiceTest {
	private static String url = "https://www.youtube.com/watch?v=jeqdYqsrsA0";

	@InjectMocks
	private MusicService musicService;

	@Mock
	private MusicRepository musicRepository;
	@Mock
	private ArtistService artistService;

	@Test
	public void createMusic() throws Exception {
		//given
		MusicForm musicForm = MusicForm.builder()
			.name("좋은 날")
			.url(url)
			.description("좋은날에 들으세요")
			.build();

		List<ArtistForm> artistList = new ArrayList<>();
		SoloArtist soloArtist = SoloArtist.builder()
			.id(1L)
			.name("IU")
			.description("국힙 원탑")
			.build();
		ArtistForm artistForm = ArtistForm.toDto(soloArtist);
		artistList.add(artistForm);

		Music music = Music.builder()
			.name(musicForm.getName())
			.description(musicForm.getDescription())
			.url(musicForm.getUrl())
			.artist(soloArtist)
			.build();
		musicForm.setArtistFormList(artistList);

		given(artistService.createArtist(any())).willReturn(soloArtist);
		given(musicRepository.save(any())).willReturn(music);
		//when
		Music savedMusic = musicService.create(musicForm);
		//then
		assertThat(savedMusic.getName()).isEqualTo(musicForm.getName());
		assertThat(savedMusic.getArtist().getName()).isEqualTo(soloArtist.getName());
		assertThat(soloArtist.getMusicList().stream().map(m -> m.getName()).toList())
			.contains(music.getName());

	}

}
