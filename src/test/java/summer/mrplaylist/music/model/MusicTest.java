package summer.mrplaylist.music.model;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import summer.mrplaylist.music.dto.MusicForm;

class MusicTest {

	private static String url = "https://www.youtube.com/watch?v=jeqdYqsrsA0";

	@Test
	public void createMusic() throws Exception {
		//given
		MusicForm musicForm = MusicForm.builder()
			.name("좋은 날")
			.url(url)
			.description("좋은날에 들으세요")
			.build();

		SoloArtist soloArtist = SoloArtist.builder()
			.id(1L)
			.name("IU")
			.description("국힙 원탑")
			.build();
		//when
		Music music = Music.createMusic(musicForm, soloArtist);
		//then
		assertThat(music.getName()).isEqualTo(musicForm.getName());
		assertThat(music.getUrl()).isEqualTo(musicForm.getUrl());
		assertThat(soloArtist.getMusicList()).contains(music);
	}
}
