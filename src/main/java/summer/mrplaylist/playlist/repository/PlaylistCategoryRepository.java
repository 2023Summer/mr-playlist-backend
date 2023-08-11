package summer.mrplaylist.playlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import summer.mrplaylist.playlist.model.PlaylistCategory;

public interface PlaylistCategoryRepository extends JpaRepository<PlaylistCategory, Long> {
}
