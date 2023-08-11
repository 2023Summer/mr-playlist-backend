package summer.mrplaylist.search.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.music.repository.MainArtistQRepo;
import summer.mrplaylist.music.repository.MusicQRepo;
import summer.mrplaylist.playlist.repository.PlaylistQRepo;
import summer.mrplaylist.search.dto.SearchCond;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

	private final PlaylistQRepo playlistQRepo;
	private final MusicQRepo musicQRepo;
	private final MainArtistQRepo mainArtistQRepo;

	public Page<?> search(SearchCond cond, Pageable pageable) {

		var resultList = switch (cond.getTopic()) {
			case MUSIC -> musicQRepo.findNameAndArtist(cond, pageable);
			case ARTIST -> mainArtistQRepo.findArtist(cond, pageable);
			case PLAYLIST -> playlistQRepo.findNameDescription(cond, pageable);
			default -> throw new IllegalStateException("검색 조건이 잘못되었습니다.");
		};

		return resultList;
	}

}
