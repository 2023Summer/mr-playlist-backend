package summer.mrplaylist.music.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import summer.mrplaylist.music.model.MainArtist;

public interface MainArtistRepository extends JpaRepository<MainArtist, Long> {

	Optional<MainArtist> findGroupByName(String name);

	Optional<MainArtist> findSoloArtistByName(String name);
}
