package summer.mrplaylist.music.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.music.model.Artist;
import summer.mrplaylist.music.model.Music;
import summer.mrplaylist.music.repository.ArtistRepository;
import summer.mrplaylist.music.repository.MusicRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MusicServiceTest {
    private static String url = "https://www.youtube.com/watch?v=jeqdYqsrsA0";

    @InjectMocks
    private MusicService musicService;

    @Mock
    private MusicRepository musicRepository;
    @Mock
    private ArtistRepository artistRepository;


    @Test
    public void createMusic() throws Exception {
        //given
        MusicForm musicForm = new MusicForm("좋은 날", url, "좋은날에 들으세요");
        Artist artist = Artist.builder()
                .id(1L)
                .name("IU")
                .description("국힙 원탑")
                .build();
        Music music = Music.builder()
                .name(musicForm.getName())
                .description(musicForm.getDescription())
                .url(musicForm.getUrl())
                .artist(artist)
                .build();
        given(artistRepository.findById(any())).willReturn(Optional.ofNullable(artist));
        given(musicRepository.save(any())).willReturn(music);
        //when
        Music savedMusic = musicService.create(musicForm, artist.getId());
        //then
        assertThat(savedMusic.getName()).isEqualTo(musicForm.getName());
        assertThat(savedMusic.getArtist().getName()).isEqualTo(artist.getName());
        assertThat(artist.getMusicList().stream().map(m->m.getName()).toList())
                .contains(music.getName());

    }

}