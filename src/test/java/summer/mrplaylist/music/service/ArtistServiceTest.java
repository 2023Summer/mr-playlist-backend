package summer.mrplaylist.music.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import summer.mrplaylist.music.dto.ArtistForm;
import summer.mrplaylist.music.model.Artist;
import summer.mrplaylist.music.repository.ArtistRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {

    @InjectMocks
    private ArtistService artistService;

    @Mock
    private ArtistRepository artistRepository;
    
    @Test
    public void createSoloArtistFirstTime() throws Exception {
        //given
        List<ArtistForm> artistForms = new ArrayList<>();
        ArtistForm artistForm = new ArtistForm("IU", "국힙 원탑");
        artistForms.add(artistForm);
        Artist artist = Artist.builder()
                .id(1L)
                .name(artistForm.getName())
                .description(artistForm.getDescription())
                .build();
        given(artistRepository.save(any())).willReturn(artist);
        //when
        Artist createdArtist = artistService.createArtist(artistForms);
        //then
        assertThat(createdArtist.getId()).isEqualTo(1L);
        assertThat(createdArtist.getName()).isEqualTo(artistForm.getName());
        assertThat(createdArtist.getDescription()).isEqualTo(artistForm.getDescription());
    }

    @Test
    public void createSoloArtistDuplicate() throws Exception {
        //given
        List<ArtistForm> artistForms = new ArrayList<>();
        ArtistForm artistForm = new ArtistForm("IU", "국민 여동생");
        artistForms.add(artistForm);

        Artist artist = Artist.builder()
                .id(1L)
                .name("IU")
                .description("국힙 원탑")
                .build();
        given(artistRepository.findArtistByName(any())).willReturn(Optional.ofNullable(artist));

        //when
        Artist duplicatedArtist = artistService.createArtist(artistForms);
        //then
        assertThat(duplicatedArtist.getId()).isEqualTo(1L);
        assertThat(duplicatedArtist.getName()).isEqualTo(artistForm.getName());
        assertThat(duplicatedArtist.getDescription())
                .isEqualTo(artist.getDescription());
    }

}