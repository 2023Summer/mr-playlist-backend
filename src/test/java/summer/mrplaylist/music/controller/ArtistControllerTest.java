package summer.mrplaylist.music.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import summer.mrplaylist.music.model.Group;
import summer.mrplaylist.music.service.MainArtistService;

@WebMvcTest(ArtistController.class)
class ArtistControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MainArtistService artistService;

	@DisplayName("[API][GET] 가수(그룹) 조회")
	@WithUserDetails
	@Test
	public void testGetGroup() throws Exception {
		//given
		given(artistService.findMainArtist(any()))
			.willReturn(Group.builder()
				.id(1L)
				.name("결속밴드")
				.description("밴드")
				.totalArtist(4)
				.build());
		//when
		mockMvc.perform(get("/api/artist/1"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data.type").value("Group"))
			.andExpect(jsonPath("$.data.groupName").value("결속밴드"));
		//then

	}

}
