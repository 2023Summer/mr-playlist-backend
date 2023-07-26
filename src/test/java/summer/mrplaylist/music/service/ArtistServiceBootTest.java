package summer.mrplaylist.music.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import summer.mrplaylist.music.dto.ArtistForm;
import summer.mrplaylist.music.model.Artist;
import summer.mrplaylist.music.repository.ArtistRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class ArtistServiceBootTest {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    public void createGroupArtist() throws Exception {
        //given
        List<ArtistForm> artistForms = new ArrayList<>();
        ArtistForm groupForm = new ArtistForm("빅뱅", "YG 인기 남그룹");
        artistForms.add(groupForm);
        artistForms.add(new ArtistForm("GD", "삐딱하게"));
        artistForms.add(new ArtistForm("태양", "Good boy"));
        //when
        Artist group = artistService.createArtist(artistForms);
        //then
        assertThat(group.getName()).isEqualTo(groupForm.getName());
        assertThat(group.getTotalArtist()).isEqualTo(2);
        assertThat(group.getGroupArtistList().stream().map(n-> n.getName()).collect(Collectors.toList())).contains("GD");

    }
}
