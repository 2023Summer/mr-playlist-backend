package summer.mrplaylist.playlist.repository;

import java.time.LocalDateTime;
import java.util.List;
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
import summer.mrplaylist.playlist.model.QPlaylist;
import summer.mrplaylist.search.dto.SearchCond;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PlaylistQRepo {

	private final JPAQueryFactory queryFactory;

	QPlaylist qPlaylist = QPlaylist.playlist;
	QMember qMember = QMember.member;

	/**
	 * 키워드 검색
	 * 우선적으로 이름을 검색한뒤
	 * 남은건 설명에서 검색한다.
	 */
	public Page<Playlist> findNameDescription(SearchCond cond, Pageable pageable) {
		List<Playlist> result = findByName(cond, pageable);
		int length = result.size();

		if (pageable.getPageSize() > length) {
			PageRequest page = PageRequest.of(0, pageable.getPageSize() - length);
			List<Playlist> byDescription = findByDescription(cond, page, result);
			result = Stream.of(result, byDescription)
				.flatMap(x -> x.stream())
				.toList();
		}

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

	public List<Playlist> orderByComment(Pageable pageable) {
		return queryFactory.selectFrom(qPlaylist)
			.leftJoin(qPlaylist.member, qMember).fetchJoin()
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(qPlaylist.commentCount.desc(), orderDate())
			.fetch();
	}

	public List<Playlist> orderByDate(Pageable pageable) {
		return queryFactory.selectFrom(qPlaylist)
			.leftJoin(qPlaylist.member, qMember).fetchJoin()
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(orderDate())
			.fetch();
	}

	private OrderSpecifier<LocalDateTime> orderDate() {
		return qPlaylist.createdAt.desc();
	}

	private BooleanExpression containName(String word) {
		return qPlaylist.name.contains(word);
	}

	private BooleanExpression containDescription(String word) {
		return qPlaylist.description.contains(word);
	}
}
