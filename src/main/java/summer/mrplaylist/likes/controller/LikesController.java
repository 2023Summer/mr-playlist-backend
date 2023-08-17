package summer.mrplaylist.likes.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.common.dto.Response;
import summer.mrplaylist.common.util.CurrentUser;
import summer.mrplaylist.likes.dto.LikesForm;
import summer.mrplaylist.likes.service.LikesService;
import summer.mrplaylist.member.model.Member;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class LikesController {
	private final LikesService likesService;
	private static final String POST_ADD_LIKE = "/like";
	private static final String DELETE_LIKE = "/like/delete";

	@PostMapping(POST_ADD_LIKE)
	public Response<String> postLike(@RequestBody LikesForm likesForm) {
		likesService.playlistAddLike(likesForm);
		return new Response("좋아요가 완료되었습니다.");
	}

	@DeleteMapping(DELETE_LIKE)
	public Response<String> deleteLike(@RequestParam("playlist_id") Long playlistId,
		@CurrentUser Member currentMember) {
		likesService.playlistDeleteLike(playlistId, currentMember.getId());
		return new Response("좋아요가 취소되었습니다.");
	}
}
