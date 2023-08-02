package summer.mrplaylist.playlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import summer.mrplaylist.playlist.model.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByName(String name);
}
