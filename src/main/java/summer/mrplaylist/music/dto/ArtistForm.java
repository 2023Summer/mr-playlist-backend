package summer.mrplaylist.music.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import summer.mrplaylist.music.model.MainArtist;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistForm {

    private String name;
    private String description;

    public static ArtistForm toDto(MainArtist mainArtist){
        return new ArtistForm(mainArtist.getName(), mainArtist.getDescription());
    }
}
