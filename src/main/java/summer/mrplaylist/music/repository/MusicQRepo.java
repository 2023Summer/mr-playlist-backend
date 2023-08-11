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
import summer.mrplaylist.music.model.Music;
import summer.mrplaylist.music.model.QMainArtist;
import summer.mrplaylist.music.model.QMusic;
import summer.mrplaylist.search.dto.SearchCond;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MusicQRepo {

	private final JPAQueryFactory queryFactory;

	QMusic qMusic = QMusic.music;
	QMainArtist qMainArtist = QMainArtist.mainArtist;

	public Page<Music> findNameAndArtist(SearchCond cond, Pageable pageable) {
		List<Music> findMusic = queryFactory.selectFrom(qMusic)
			.join(qMusic.artist, qMainArtist)
			.where(containName(cond.getWord())
				.or(likeArtist(cond)))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy()
			.fetch();

		return new PageImpl<>(findMusic, pageable, findMusic.size());
	}

	private BooleanExpression likeArtist(SearchCond cond) {
		return qMusic.artist.name.contains(cond.getWord());
	}

	private BooleanExpression containName(String word) {
		return qMusic.name.like(word);
	}
}
