package summer.mrplaylist.likes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import summer.mrplaylist.likes.model.Likes;

public interface LikesRepository extends JpaRepository<Likes, Long> {
	@Query(value = "select l from Likes l where l.playlistId.id =:playlistId and l.memberId.id =:memberId")
	Optional<Likes> findByPlaylistIdAndMemberId(Long playlistId, Long memberId);

}
