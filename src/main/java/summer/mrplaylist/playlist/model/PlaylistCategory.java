package summer.mrplaylist.playlist.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class PlaylistCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public static PlaylistCategory createPlayListCategory(Playlist playlist, Category category){
        return PlaylistCategory.builder()
                .playlist(playlist)
                .category(category)
                .build();
    }


}
