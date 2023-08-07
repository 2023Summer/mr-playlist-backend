package summer.mrplaylist.playlist.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import summer.mrplaylist.playlist.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByName(String name);
}
