package summer.mrplaylist.likes.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import summer.mrplaylist.likes.model.Likes;
import summer.mrplaylist.likes.model.QLikes;

@Repository
@RequiredArgsConstructor
public class LikesQRepo {
	private final JPAQueryFactory queryFactory;

	QLikes qLikes = QLikes.likes;

	public List<Likes> findByPlaylistId(Long playlistId) {
		return queryFactory
			.selectFrom(qLikes)
			.where(
				qLikes.playlist.id.eq(playlistId))
			.fetch();
	}

	public boolean existsByPlaylistIdAndMemberId(Long playlistId, Long memberId) {
		Likes like = queryFactory
			.selectFrom(qLikes)
			.where(
				qLikes.playlist.id.eq(playlistId), qLikes.member.id.eq(memberId))
			.fetchFirst();

		return like != null;
	}
}
