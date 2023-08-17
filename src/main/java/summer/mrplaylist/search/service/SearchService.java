package summer.mrplaylist.search.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.common.constant.RedisConstants;
import summer.mrplaylist.common.service.RedisService;
import summer.mrplaylist.music.repository.MainArtistQRepo;
import summer.mrplaylist.music.repository.MusicQRepo;
import summer.mrplaylist.playlist.repository.PlaylistQRepo;
import summer.mrplaylist.search.dto.SearchCond;
import summer.mrplaylist.search.dto.SearchResponse;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

	private final PlaylistQRepo playlistQRepo;
	private final MusicQRepo musicQRepo;
	private final MainArtistQRepo mainArtistQRepo;
	private final RedisService redisService;

	public Page<SearchResponse> search(SearchCond cond, Pageable pageable) {

		redisService.getZset().incrementScore(RedisConstants.SEARCH, cond.getWord(), 1);

		Page<SearchResponse> resultList = switch (cond.getTopic()) {
			case MUSIC -> musicQRepo.findNameAndArtist(cond, pageable);
			case ARTIST -> mainArtistQRepo.findArtist(cond, pageable);
			case PLAYLIST -> playlistQRepo.findNameDescription(cond, pageable);
			case CATEGORY -> playlistQRepo.findHavingCategory(cond, pageable);
			default -> throw new IllegalStateException("검색 조건이 잘못되었습니다.");
		};

		return resultList;
	}

	public Page findTopWords(Pageable pageable) {
		ZSetOperations<String, String> zSet =
			redisService.getZset();

		Set<ZSetOperations.TypedTuple<String>> searchWord = zSet.reverseRangeWithScores(RedisConstants.SEARCH,
			pageable.getOffset(),
			pageable.getPageSize());

		List<String> findTopWords = searchWord.stream().map(word -> word.getValue()).toList();
		return new PageImpl(findTopWords, pageable, findTopWords.size());
	}

}
