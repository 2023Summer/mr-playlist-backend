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

	public static Member getMember2() {
		Member member = Member.builder()
			.email("test2@naver.com")
			.password("abc")
			.nickname("jpop2")
			.profileImg("abcde")
			.role(Role.USER)
			.build();
		return member;
	}

	public static List<MusicForm> getMusicFormList() {
		List<MusicForm> musicFormList = new ArrayList<>();

		MusicForm musicForm = getMusicForm("킹 누", "일본 5인조", "역몽", "주술회전0 ost");

		musicFormList.add(musicForm);

		return musicFormList;
	}

	public static MusicForm getMusicForm(String groupName, String groupDescription, String musicName,
		String musicDescription) {

		GroupForm groupForm = new GroupForm(groupName, groupDescription);
		List<ArtistForm> artistFormList = new ArrayList<>();
		artistFormList.add(new ArtistForm("츠네다 다이키", "1번"));
		artistFormList.add(new ArtistForm("아라이 카즈키", "2번"));
		MusicForm musicForm = MusicForm.builder()
			.name(musicName)
			.description(musicDescription)
			.url("sing_youtube")
			.groupForm(groupForm)
			.artistFormList(artistFormList)
			.build();
		return musicForm;
	}

	public static PlaylistForm getPlaylistForm(Member member) {

		PlaylistForm playlistForm = PlaylistForm.builder()
			.plName("킹 누의 명곡")
			.plDescription("노동요")
			.categoryNameList(getCategoryNameList())
			.musicFormList(getMusicFormList())
			.member(member)
			.build();

		return playlistForm;
	}

}
