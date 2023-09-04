package summer.mrplaylist.music.controller;

import static summer.mrplaylist.music.controller.MusicController.Message.*;

import jakarta.mail.Multipart;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import summer.mrplaylist.common.dto.Response;
import summer.mrplaylist.common.service.FileUploadService;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.music.dto.MusicUpdateForm;
import summer.mrplaylist.music.model.Music;
import summer.mrplaylist.music.service.MusicService;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class MusicController {

	private final MusicService musicService;
	private final FileUploadService fileUploadService;
	private static final String POST_REGISTER_MUSIC = "/music/register";
	private static final String POST_UPDATE_MUSIC = "/music/update";
	private static final String DELETE_MUSIC = "/music/update";

	@PostMapping(POST_REGISTER_MUSIC)
	//public Response registerMusic(@RequestBody MusicForm musicForm) {
	public Response registerMusic(@RequestPart("musicForm") MusicForm musicForm, @RequestPart("musicImg") MultipartFile musicImg) {
		String imageUrl;
		if(musicImg.isEmpty()){
			imageUrl = "https://www.cheonyu.com/_DATA/product/60900/60960_1666159152.jpg";
		}
		else{
			imageUrl = fileUploadService.save(musicImg, "aws test/").getImgUrl();
		}
		musicForm.setImgUrl(imageUrl);
		Music music = musicService.create(musicForm);
		return createResponse(music, "음악 저장을 성공했습니다.");
	}

	@PostMapping(POST_UPDATE_MUSIC)
	public Response updateMusic(@RequestBody MusicUpdateForm musicUpdateForm) {
		Music music = musicService.update(musicUpdateForm);
		return createResponse(music, "음악 수정을 성공했습니다.");
	}

	@DeleteMapping(DELETE_MUSIC)
	public Response deleteMusic(@RequestParam(name = "id") Long musicId) {
		musicService.delete(musicId);
		return new Response("성공적으로 음악을 삭제했습니다.");
	}

	@Data
	@AllArgsConstructor
	@Builder
	static class Message {
		private Long id;
		private String message;

		public static Response createResponse(Music music, String message) {
			Message msg = builder()
				.id(music.getId())
				.message(message)
				.build();
			return new Response(msg);
		}
	}
}
