package summer.mrplaylist.music.model;

import org.junit.jupiter.api.Test;
import summer.mrplaylist.music.dto.ArtistForm;

import static org.assertj.core.api.Assertions.*;

class ArtistTest {

    @Test
    public void createArtistTest() throws Exception {
        //given
        ArtistForm artistForm = createArtistForm("IU", "국힙 원탑");
        //when
        Artist artist = Artist.createArtist(artistForm);
        //then
        assertThat(artist.getTotalArtist()).isEqualTo(0);
        assertThat(artist.getName()).isEqualTo(artistForm.getName());
        assertThat(artist.getDescription()).isEqualTo(artistForm.getDescription());
        assertThat(artist.getGroupArtist()).isEqualTo(null);
    }

    @Test
    public void addArtistGroup() throws Exception {
        //given
        ArtistForm groupForm = createArtistForm("빅뱅", "YG");
        ArtistForm artistForm = createArtistForm("GD", "삐딱함");
        Artist group = Artist.createArtist(groupForm);
        Artist artist = Artist.createArtist(artistForm);
        //when
        group.addArtist(artist);
        //then
        assertThat(group.getGroupArtistList()).contains(artist);
        assertThat(artist.getGroupArtist()).isEqualTo(group);
        assertThat(group.getTotalArtist()).isEqualTo(1);
    }

    private static ArtistForm createArtistForm(String name, String descript) {
        ArtistForm artistForm = new ArtistForm(name, descript);
        return artistForm;
    }

}