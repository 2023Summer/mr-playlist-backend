package summer.mrplaylist.playlist.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.member.model.QMember;
import summer.mrplaylist.playlist.model.Playlist;
import summer.mrplaylist.playlist.model.QCategory;
import summer.mrplaylist.playlist.model.QPlaylist;
import summer.mrplaylist.playlist.model.QPlaylistCategory;
import summer.mrplaylist.search.dto.SearchCond;
import summer.mrplaylist.search.dto.SearchResponse;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PlaylistQRepo {

	private final JPAQueryFactory queryFactory;

	QPlaylist qPlaylist = QPlaylist.playlist;
	QMember qMember = QMember.member;
	QPlaylistCategory qPlaylistCategory = QPlaylistCategory.playlistCategory;
	QCategory qCategory = QCategory.category;

	/**
	 * 키워드 검색
	 * 우선적으로 이름을 검색한뒤
	 * 남은건 설명에서 검색한다.
	 */
	public Page<SearchResponse> findNameDescription(SearchCond cond, Pageable pageable) {
		List<Playlist> result = findByName(cond, pageable);
		int length = result.size();

		if (pageable.getPageSize() > length) {
			PageRequest page = PageRequest.of(0, pageable.getPageSize() - length);
			List<Playlist> byDescription = findByDescription(cond, page, result);
			result = Stream.of(result, byDescription)
				.flatMap(x -> x.stream())
				.toList();
		}

		List<SearchResponse> responses = result.stream()
			.map(r -> new SearchResponse(r.getId(), r.getName(), r.getDescription()))
			.collect(Collectors.toList());

		return new PageImpl<>(responses, pageable, responses.size());
	}

	public Page<Playlist> findHavingCategory(String word, Pageable pageable) {
		List<Playlist> result = queryFactory.select(qPlaylistCategory.playlist)
			.from(qPlaylistCategory)
			.leftJoin(qPlaylistCategory.playlist, qPlaylist)
			.leftJoin(qPlaylistCategory.category, qCategory)
			.where(qCategory.name.eq(word))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return new PageImpl<>(result, pageable, result.size());
	}

	public List<Playlist> findByName(SearchCond cond, Pageable pageable) {
		return queryFactory.selectFrom(qPlaylist)
			.leftJoin(qPlaylist.member, qMember).fetchJoin()
			.where(containName(cond.getWord()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	public List<Playlist> findByDescription(SearchCond cond, Pageable pageable, List<Playlist> findList) {
		return queryFactory.selectFrom(qPlaylist)
			.leftJoin(qPlaylist.member, qMember).fetchJoin()
			.where(containName(cond.getWord()),
				qPlaylist.notIn(findList))
			.limit(pageable.getPageSize())
			.fetch();
	}

	public List<Playlist> orderByCond(String cond, Pageable pageable) {
		return queryFactory.selectFrom(qPlaylist)
			.leftJoin(qPlaylist.member, qMember).fetchJoin()
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(orderComment(cond), orderDate())
			.fetch();
	}

	private OrderSpecifier<Integer> orderComment(String cond) {
		if (cond.equals("comment"))
			return qPlaylist.commentCount.desc();
		else
			return null;
	}

	private OrderSpecifier<LocalDateTime> orderDate() {
		return qPlaylist.createdAt.desc();
	}

	private BooleanExpression containName(String word) {
		return qPlaylist.name.contains(word);
	}

}
