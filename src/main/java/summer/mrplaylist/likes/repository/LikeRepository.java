package summer.mrplaylist.likes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import summer.mrplaylist.likes.model.Likes;

public interface LikeRepository extends JpaRepository<Likes, Long> {
}
