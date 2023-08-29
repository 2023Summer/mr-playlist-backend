package summer.mrplaylist.likes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import summer.mrplaylist.likes.model.Likes;

public interface LikesRepository extends JpaRepository<Likes, Long> {
	@Query(value = "select l from Likes l where l.playlist.id =:playlistId and l.member.id =:memberId")
	Optional<Likes> findByPlaylistIdAndMemberId(@Param("playlistId") Long playlistId,
		@Param("memberId") Long memberId);
}
