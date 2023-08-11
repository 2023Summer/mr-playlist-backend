package summer.mrplaylist;

import java.util.ArrayList;
import java.util.List;

import summer.mrplaylist.member.constant.Role;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.music.dto.ArtistForm;
import summer.mrplaylist.music.dto.GroupForm;
import summer.mrplaylist.music.dto.MusicForm;
import summer.mrplaylist.playlist.dto.PlaylistForm;

public class CreateMethod {

	public static List<String> getCategoryNameList() {
		List<String> categoryNameList = new ArrayList<>();
		categoryNameList.add("일본 밴드");
		categoryNameList.add("몽환");
		return categoryNameList;
	}

	// 추후 createMember 로 변경
	public static Member getMember() {
		Member member = Member.builder()
			.email("test@naver.com")
			.password("abc")
			.nickname("jpop")
			.profileImg("abcde")
			.role(Role.USER)
			.build();
		return member;
	}

	public static List<MusicForm> getMusicFormList() {
		List<MusicForm> musicFormList = new ArrayList<>();

		MusicForm musicForm = getMusicForm();

		musicFormList.add(musicForm);

		return musicFormList;
	}

	public static MusicForm getMusicForm() {
		GroupForm groupForm = new GroupForm("킹 누", "일본 5인조");
		List<ArtistForm> artistFormList = new ArrayList<>();
		artistFormList.add(new ArtistForm("츠네다 다이키", "1번"));
		artistFormList.add(new ArtistForm("아라이 카즈키", "2번"));
		MusicForm musicForm = MusicForm.builder()
			.name("역몽")
			.description("주술회전0 ost")
			.url("sing_youtube")
			.groupForm(groupForm)
			.artistFormList(artistFormList)
			.build();
		return musicForm;
	}

	public static PlaylistForm getPlaylistForm(Member member) {
		List<String> categoryNameList = getCategoryNameList();

		PlaylistForm playlistForm = PlaylistForm.builder()
			.plName("킹 누의 명곡")
			.plDescription("노동요")
			.categoryNameList(categoryNameList)
			.member(member)
			.build();

		return playlistForm;
	}

}
