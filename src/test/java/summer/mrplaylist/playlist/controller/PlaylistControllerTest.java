package summer.mrplaylist.playlist.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static summer.mrplaylist.CreateMethod.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.playlist.dto.PlaylistForm;
import summer.mrplaylist.playlist.dto.PlaylistSimpleResponse;
import summer.mrplaylist.playlist.model.Playlist;
import summer.mrplaylist.playlist.service.PlaylistService;

@WebMvcTest(PlaylistController.class)
class PlaylistControllerTest {

	@Autowired
	private MockMvc mockmvc;

	@MockBean
	private PlaylistService playlistService;

	protected MediaType contentType =
		new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

	@DisplayName("[API][POST]플레이리스트 생성 + 노래")
	@WithMockUser
	@Test
	public void testPostRegister() throws Exception {
		Member member = getMember();
		PlaylistForm playlistForm = getPlaylistForm(member);
		//given
		given(playlistService.create(any()))
			.willReturn(
				Playlist.builder()
					.id(1L)
					.commentCount(0)
					.musicCount(0)
					.description("동심가득")
					.build()
			);
		//when
		mockmvc.perform(post("/api/playlist/register")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content("{\n"
					+ "    \"plName\" : \"디즈니 플레이 리스트\",\n"
					+ "    \"plDescription\": \"동심가득\",\n"
					+ "    \"categoryNameList\": [\n"
					+ "        \"동심\",\n"
					+ "        \"행복\"\n"
					+ "    ],\n"
					+ "    \"musicFormList\":[\n"
					+ "        {\n"
					+ "            \"name\": \"Let it go\",\n"
					+ "            \"url\": \"test.com\",\n"
					+ "            \"description\":\"겨울왕국\",\n"
					+ "            \"groupFrom\": null,\n"
					+ "            \"artistFormList\":[\n"
					+ "                {\n"
					+ "                    \"name\": \"엘사\",\n"
					+ "                    \"description\": \"겨울왕국 공주\"\n"
					+ "                }\n"
					+ "            ] \n"
					+ "        }\n"
					+ "    ]\n"
					+ "\n"
					+ "}")
				.with(csrf())
			).andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data.message").value("성공적으로 저장했습니다."))
			.andExpect(jsonPath("$.data.id").value(1));

	}

	@DisplayName("[API][GET] 조건별 플레이리스트 검색")
	@WithMockUser
	@Test
	public void testGetOrderInfo() throws Exception {
		List<PlaylistSimpleResponse> arrayList = new ArrayList<>();
		arrayList.add(new PlaylistSimpleResponse(1L, "가정교사히트맨리본", "2021-01-01", 3));
		arrayList.add(new PlaylistSimpleResponse(2L, "은혼", "2021-01-06", 5));

		PageRequest page = PageRequest.of(0, 10);
		//given
		given(playlistService.findPlaylistOrderByCond("comment", page))
			.willReturn(
				arrayList
			);
		//when
		mockmvc.perform(get("/api/playlist/comment/order?size=10&page=0")
			).andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data[0].id").value(1L))
			.andExpect(jsonPath("$.data[0].name").value("가정교사히트맨리본"))
			.andExpect(jsonPath("$.data[1].name").value("은혼"));
	}

}
