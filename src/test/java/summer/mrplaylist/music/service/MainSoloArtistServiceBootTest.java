package summer.mrplaylist.music.service;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import summer.mrplaylist.music.dto.ArtistForm;
import summer.mrplaylist.music.dto.GroupForm;
import summer.mrplaylist.music.model.Group;
import summer.mrplaylist.music.model.MainArtist;
import summer.mrplaylist.music.repository.MainArtistRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@SpringBootTest
public class MainSoloArtistServiceBootTest {

	@Autowired
	private ArtistService artistService;

	@Test
	public void createGroupArtist() throws Exception {
		//given
		List<ArtistForm> artistForms = new ArrayList<>();
		GroupForm groupForm = new GroupForm("빅뱅", "YG 인기 남그룹");
		artistForms.add(new ArtistForm("GD", "삐딱하게"));
		artistForms.add(new ArtistForm("태양", "Good boy"));
		//when
		Group group = artistService.createGroupArtist(groupForm, artistForms);
		//then
		assertThat(group.getName()).isEqualTo(groupForm.getGroupName());
		assertThat(group.getTotalArtist()).isEqualTo(2);
		assertThat(group.getGroupSoloArtistList().stream().map(n -> n.getName()).collect(Collectors.toList())).contains(
			"GD");

		log.info("getSoloArtist = {}", group.getGroupSoloArtistList());
	}
}
