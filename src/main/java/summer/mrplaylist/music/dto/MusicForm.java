package summer.mrplaylist.music.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MusicForm {
    private String name;
    private String url;
    private String description;
}
