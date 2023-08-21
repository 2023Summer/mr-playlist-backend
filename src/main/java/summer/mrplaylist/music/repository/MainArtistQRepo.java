package summer.mrplaylist.music.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.music.model.MainArtist;
import summer.mrplaylist.music.model.QMainArtist;
import summer.mrplaylist.search.dto.SearchCond;
import summer.mrplaylist.search.dto.SearchResponse;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MainArtistQRepo {
	private final JPAQueryFactory queryFactory;

	QMainArtist qMainArtist = QMainArtist.mainArtist;

	public Page<SearchResponse> findArtist(SearchCond cond, Pageable pageable) {
		List<MainArtist> findArtist = queryFactory.selectFrom(qMainArtist)
			.where(containName(cond.getWord()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		List<SearchResponse> searchResponses = findArtist.stream().map(artist ->
				new SearchResponse(artist.getId(), artist.getName(), artist.getDescription()))
			.toList();

		return new PageImpl<>(searchResponses, pageable, searchResponses.size());
	}

	private BooleanExpression containName(String word) {
		return qMainArtist.name.contains(word);
	}
}
