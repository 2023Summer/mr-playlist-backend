package summer.mrplaylist.playlist.controller;

import static summer.mrplaylist.playlist.controller.PlaylistController.Message.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import summer.mrplaylist.common.dto.Response;
import summer.mrplaylist.playlist.dto.PlaylistForm;
import summer.mrplaylist.playlist.dto.PlaylistResponse;
import summer.mrplaylist.playlist.dto.PlaylistSimpleResponse;
import summer.mrplaylist.playlist.dto.PlaylistUpdateForm;
import summer.mrplaylist.playlist.model.Playlist;
import summer.mrplaylist.playlist.service.PlaylistService;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class PlaylistController {

	private final PlaylistService playlistService;

	private static final String POST_REGISTER_PLAYLIST = "/playlist/register";
	private static final String POST_UPDATE_PLAYLIST = "/playlist/update";
	private static final String DELETE_PLAYLIST = "/playlist/delete";
	private static final String GET_PLAYLIST_INFO = "/playlist/{playlistId}";
	private static final String GET_PLAYLIST_ORDER_BY_COND = "/playlist/{cond}/order";

	private static final String GET_PLAYLIST_BY_CATEGORY = "/playlist/find-category";

	@PostMapping(POST_REGISTER_PLAYLIST)
	public Response registerPlaylist(@RequestBody PlaylistForm playlistForm) {
		Playlist playlist = playlistService.create(playlistForm);
		return createResponse(playlist, "성공적으로 저장했습니다.");
	}

	@PostMapping(POST_UPDATE_PLAYLIST)
	public Response updatePlaylist(@RequestBody PlaylistUpdateForm playlistUpdateForm) {
		Playlist playlist = playlistService.updateInfo(playlistUpdateForm);
		return createResponse(playlist, "성공적으로 수정했습니다.");
	}

	@DeleteMapping(DELETE_PLAYLIST)
	public Response deletePlaylist(@RequestParam(name = "playlistId") Long playlistId) {
		playlistService.delete(playlistId);
		return new Response("성공적으로 삭제했습니다.");
	}

	@GetMapping(GET_PLAYLIST_INFO)
	public Response getPlaylistInfo(@PathVariable(name = "playlistId") Long playlistId) {
		PlaylistResponse playlistInfo = playlistService.findPlaylistInfo(playlistId);
		return new Response(playlistInfo);
	}

	@GetMapping(GET_PLAYLIST_ORDER_BY_COND)
	public Response getPlaylistOrderByComment(@PathVariable(name = "cond") String cond,
		@PageableDefault(size = 5, page = 0) Pageable pageable) {
		List<PlaylistSimpleResponse> playlistResponse = playlistService.findPlaylistOrderByCond(cond, pageable);
		return new Response(playlistResponse);
	}

	@GetMapping(GET_PLAYLIST_BY_CATEGORY)
	public Response getPlaylistByCategory(@RequestParam String categoryName,
		@PageableDefault(size = 5, page = 0) Pageable pageable) {
		List<PlaylistSimpleResponse> playlistByCategory = playlistService.getPlaylistByCategory(categoryName, pageable);
		return new Response(playlistByCategory);
	}

	@Data
	@AllArgsConstructor
	@Builder
	static class Message {
		private Long id;
		private String message;

		public static Response createResponse(Playlist playlist, String message) {
			Message msg = builder()
				.id(playlist.getId())
				.message(message)
				.build();
			return new Response(msg);
		}
	}
}
