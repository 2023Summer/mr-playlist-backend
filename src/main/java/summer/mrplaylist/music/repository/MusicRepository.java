package summer.mrplaylist.music.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import summer.mrplaylist.music.model.Music;

public interface MusicRepository extends JpaRepository<Music,Long> {
}
