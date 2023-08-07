package summer.mrplaylist.comment.dto;

import lombok.*;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.music.dto.ArtistForm;
import summer.mrplaylist.music.model.MainArtist;
import summer.mrplaylist.playlist.model.Playlist;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class CommentForm {
    private Member member;
    private Playlist playlist;
    private String content;
    private LocalDateTime createdAt;

    public static ArtistForm toDto(MainArtist mainArtist){
        return new ArtistForm(mainArtist.getName(), mainArtist.getDescription());
    }
}