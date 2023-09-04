package summer.mrplaylist.music.controller;

import static summer.mrplaylist.music.controller.ArtistController.Message.*;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import summer.mrplaylist.common.dto.Response;
import summer.mrplaylist.common.service.FileUploadService;
import summer.mrplaylist.music.dto.ArtistForm;
import summer.mrplaylist.music.dto.ArtistUpdateForm;
import summer.mrplaylist.music.dto.GroupForm;
import summer.mrplaylist.music.dto.GroupResponse;
import summer.mrplaylist.music.dto.SoloArtistResponse;
import summer.mrplaylist.music.model.Group;
import summer.mrplaylist.music.model.MainArtist;
import summer.mrplaylist.music.model.SoloArtist;
import summer.mrplaylist.music.service.MainArtistService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ArtistController {

	private final MainArtistService artistService;
	private final FileUploadService fileUploadService;

	private static final String POST_JOIN_ARTIST = "/artist/register";
	private static final String POST_JOIN_GROUP_ARTIST = "/artist/group/register";
	private static final String POST_UPDATE_ARTIST = "/artist/update";
	private static final String GET_ARTIST_INFO = "/artist/{artistId}";
	private static final String DELETE_ARTIST = "/artist/delete";

	@PostMapping(POST_JOIN_ARTIST)
	public Response joinArtist(@RequestPart("artistForm") ArtistForm artistForm,
							   @RequestPart("artistImage") MultipartFile artistImage) {
	//public Response joinArtist(@RequestBody ArtistForm artistForm) {
		String imageUrl;
		if(artistImage.isEmpty()){
			imageUrl = "https://www.cheonyu.com/_DATA/product/60900/60960_1666159152.jpg";
		}
		else{
			imageUrl = fileUploadService.save(artistImage, "aws test/").getImgUrl();
		}
		artistForm.setImgUrl(imageUrl);
		SoloArtist artist = artistService.createArtist(artistForm);
		return createResponse(artist, "성공적으로 저장했습니다.");
	}

	@PostMapping(POST_JOIN_GROUP_ARTIST)
	public Response joinGroupArtist(@RequestBody GroupForm groupForm,
		@RequestBody List<ArtistForm> artistFormList) {
		Group group = artistService.createGroupArtist(groupForm, artistFormList);
		return createResponse(group, "성공적으로 그룹과 가수를 저장했습니다.");
	}

	@PostMapping(POST_UPDATE_ARTIST)
	public Response updateArtist(@RequestBody ArtistUpdateForm artistUpdateForm) {
		MainArtist artist = artistService.update(artistUpdateForm);
		return createResponse(artist, "성공적으로 수정했습니다.");
	}

	@GetMapping(GET_ARTIST_INFO)
	public Response getArtistInfo(@PathVariable(name = "artistId") Long artistId) {
		MainArtist mainArtist = artistService.findMainArtist(artistId);
		if (mainArtist instanceof Group)
			return new Response(GroupResponse.toResponse((Group)mainArtist));
		else
			return new Response(SoloArtistResponse.toResponse((SoloArtist)mainArtist));
	}

	@DeleteMapping(DELETE_ARTIST)
	public Response deleteArtist(@RequestParam(name = "id") Long artistId) {
		artistService.deleteArtist(artistId);
		return new Response("성공적으로 삭제했습니다.");
	}

	@Data
	@AllArgsConstructor
	@Builder
	static class Message {
		private Long id;
		private String type;
		private String message;

		public static Response createResponse(MainArtist artist, String message) {
			Message msg = builder()
				.id(artist.getId())
				.type(artist instanceof Group ? "group" : "arist")
				.message(message)
				.build();
			return new Response(msg);
		}
	}

}
