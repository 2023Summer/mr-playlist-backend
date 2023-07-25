package summer.mrplaylist.music.model;

import org.junit.jupiter.api.Test;
import summer.mrplaylist.music.dto.MusicForm;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MusicTest {

    private static String url = "https://www.youtube.com/watch?v=jeqdYqsrsA0";

    @Test
    public void createMusic() throws Exception {
        //given
        MusicForm musicForm = new MusicForm("좋은 날", url, "좋은날에 들으세요");
        Artist artist = Artist.builder()
                .id(1L)
                .name("IU")
                .description("국힙 원탑")
                .build();
        //when
        Music music = Music.createMusic(musicForm, artist);
        //then
        assertThat(music.getName()).isEqualTo(musicForm.getName());
        assertThat(music.getUrl()).isEqualTo(musicForm.getUrl());
        assertThat(artist.getMusicList()).contains(music);
    }
}