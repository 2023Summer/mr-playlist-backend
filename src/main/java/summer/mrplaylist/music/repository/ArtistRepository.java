package summer.mrplaylist.music.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import summer.mrplaylist.music.model.Artist;

public interface ArtistRepository extends JpaRepository<Artist,Long> {
}
