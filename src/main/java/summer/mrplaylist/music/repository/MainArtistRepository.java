package summer.mrplaylist.music.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import summer.mrplaylist.music.model.Group;
import summer.mrplaylist.music.model.MainArtist;
import summer.mrplaylist.music.model.SoloArtist;

import java.util.Optional;

public interface MainArtistRepository extends JpaRepository<MainArtist, Long> {

	Optional<MainArtist> findGroupByName(String name);

	Optional<MainArtist> findSoloArtistByName(String name);
}
