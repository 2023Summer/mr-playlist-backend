package summer.mrplaylist.music.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import summer.mrplaylist.music.model.Artist;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist,Long> {

    Optional<Artist> findArtistByName(String name);
}
