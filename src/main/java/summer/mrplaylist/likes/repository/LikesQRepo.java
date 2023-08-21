package summer.mrplaylist.likes.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import summer.mrplaylist.likes.model.Likes;
import summer.mrplaylist.likes.model.QLikes;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikesQRepo {
	private final JPAQueryFactory queryFactory;

	QLikes qLikes = QLikes.likes;

	//@Query(value = "select l from Likes l where l.playlistId.id =:playlistId and l.memberId.id =:memberId")
	public List<Likes> findByPlaylistId(Long playlistId) {
		return queryFactory
			.select(qLikes)
			.from(qLikes)
			.where(
				qLikes.playlist.id.eq(playlistId))
			.fetch();
	}

	public boolean existsByPlaylistIdAndMemberId(Long playlistId, Long memberId) {
		Integer like = queryFactory
			.selectOne()
			.from(qLikes)
			.where(
				qLikes.playlist.id.eq(playlistId)
					.and(qLikes.member.id.eq(memberId)))
			.fetchOne();

		return like != null;
	}
}
