package summer.mrplaylist.music.dto;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class MusicForm {
    private String name;
    private String url;
    private String description;

    private GroupForm groupForm;
    private List<ArtistForm> artistFormList;
}
