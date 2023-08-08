package summer.mrplaylist.music.service;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.music.dto.ArtistForm;
import summer.mrplaylist.music.dto.GroupForm;
import summer.mrplaylist.music.model.Group;

@Slf4j
@Transactional
@SpringBootTest
public class MainArtistServiceBootTest {

	@Autowired
	private MainArtistService mainArtistService;

	@Test
	public void createGroupArtist() throws Exception {
		//given
		List<ArtistForm> artistForms = new ArrayList<>();
		GroupForm groupForm = new GroupForm("빅뱅", "YG 인기 남그룹");
		artistForms.add(new ArtistForm("GD", "삐딱하게"));
		artistForms.add(new ArtistForm("태양", "Good boy"));
		//when
		Group group = mainArtistService.createGroupArtist(groupForm, artistForms);
		//then
		assertThat(group.getName()).isEqualTo(groupForm.getGroupName());
		assertThat(group.getTotalArtist()).isEqualTo(2);
		assertThat(group.getGroupSoloArtistList().stream().map(n -> n.getName()).collect(Collectors.toList())).contains(
			"GD");

		log.info("getSoloArtist = {}", group.getGroupSoloArtistList());
	}

	@Test
	@DisplayName("같은 그룹에 같은 멤버들이 들어왔을때 멤버수")
	public void duplicate_member() throws Exception {

		List<ArtistForm> artistForms = new ArrayList<>();
		GroupForm groupForm = new GroupForm("빅뱅", "YG 인기 남그룹");
		artistForms.add(new ArtistForm("GD", "삐딱하게"));
		artistForms.add(new ArtistForm("태양", "Good boy"));
		mainArtistService.createGroupArtist(groupForm, artistForms);
		//when
		Group group = mainArtistService.createGroupArtist(groupForm, artistForms);

		assertThat(group.getTotalArtist()).isEqualTo(2);

		log.info("getSoloArtist = {}", group.getGroupSoloArtistList());
	}
}
