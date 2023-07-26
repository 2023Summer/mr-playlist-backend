package summer.mrplaylist.music.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import summer.mrplaylist.music.model.Artist;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistForm {

    private String name;
    private String description;

    public static ArtistForm toDto(Artist artist){
        return new ArtistForm(artist.getName(), artist.getDescription());
    }
}
