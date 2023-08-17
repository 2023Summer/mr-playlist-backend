package summer.mrplaylist.playlist.controller;

import static summer.mrplaylist.playlist.controller.PlaylistController.Message.*;

import org.springframework.web.bind.annotation.DeleteMapping;
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
	public Response deletePlaylist(@RequestParam(name = "id") Long playlistId) {
		playlistService.delete(playlistId);
		return new Response("성공적으로 삭제했습니다.");
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
