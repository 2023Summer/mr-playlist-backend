package summer.mrplaylist.playlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import summer.mrplaylist.playlist.model.Playlist;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
