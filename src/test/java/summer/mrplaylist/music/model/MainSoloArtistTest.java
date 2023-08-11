package summer.mrplaylist.music.model;

import org.junit.jupiter.api.Test;

import summer.mrplaylist.music.dto.ArtistForm;
import summer.mrplaylist.music.dto.GroupForm;

import static org.assertj.core.api.Assertions.*;

class MainSoloArtistTest {

	@Test
	public void createArtistTest() throws Exception {
		//given
		ArtistForm artistForm = createArtistForm("IU", "국힙 원탑");
		//when
		SoloArtist artist = SoloArtist.createArtist(artistForm);
		//then
		assertThat(artist.getName()).isEqualTo(artistForm.getName());
		assertThat(artist.getDescription()).isEqualTo(artistForm.getDescription());
	}

	@Test
	public void addArtistGroup() throws Exception {
		//given
		GroupForm groupForm = new GroupForm("빅뱅", "YG");
		ArtistForm artistForm = createArtistForm("GD", "삐딱함");
		Group group = Group.createGroup(groupForm);
		SoloArtist artist = SoloArtist.createArtist(artistForm);
		//when
		group.addArtist(artist);
		//then
		assertThat(group.getGroupSoloArtistList()).contains(artist);
		assertThat(artist.getGroup()).isEqualTo(group);
		assertThat(group.getTotalArtist()).isEqualTo(1);
	}

	private static ArtistForm createArtistForm(String name, String descript) {
		ArtistForm artistForm = new ArtistForm(name, descript);
		return artistForm;
	}

}