package summer.mrplaylist.music.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import summer.mrplaylist.music.dto.ArtistForm;
import summer.mrplaylist.music.model.MainArtist;
import summer.mrplaylist.music.model.SoloArtist;
import summer.mrplaylist.music.repository.MainArtistRepository;

@ExtendWith(MockitoExtension.class)
class MainSoloArtistServiceTest {

	@InjectMocks
	private MainArtistService artistService;

	@Mock
	private MainArtistRepository mainArtistRepository;

	@Test
	public void createSoloArtistFirstTime() throws Exception {
		//given
		ArtistForm artistForm = new ArtistForm("IU", "국힙 원탑");
		MainArtist mainArtist = SoloArtist.builder()
			.id(1L)
			.name(artistForm.getName())
			.description(artistForm.getDescription())
			.build();
		given(mainArtistRepository.save(any())).willReturn(mainArtist);
		//when
		MainArtist createdMainArtist = artistService.createArtist(artistForm);
		//then
		assertThat(createdMainArtist.getId()).isEqualTo(1L);
		assertThat(createdMainArtist.getName()).isEqualTo(artistForm.getName());
		assertThat(createdMainArtist.getDescription()).isEqualTo(artistForm.getDescription());
	}

	@Test
	public void createSoloArtistDuplicate() throws Exception {
		//given
		ArtistForm artistForm = new ArtistForm("IU", "국민 여동생");

		SoloArtist soloArtist = SoloArtist.builder()
			.id(1L)
			.name("IU")
			.description("국힙 원탑")
			.build();
		given(mainArtistRepository.findSoloArtistByName(any())).willReturn(Optional.ofNullable(soloArtist));

		//when
		MainArtist duplicatedMainArtist = artistService.createArtist(artistForm);
		//then
		assertThat(duplicatedMainArtist.getId()).isEqualTo(1L);
		assertThat(duplicatedMainArtist.getName()).isEqualTo(artistForm.getName());
		assertThat(duplicatedMainArtist.getDescription())
			.isEqualTo(soloArtist.getDescription());
	}

}
