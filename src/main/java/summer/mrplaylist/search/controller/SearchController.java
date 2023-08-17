package summer.mrplaylist.search.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.common.dto.Response;
import summer.mrplaylist.search.service.SearchService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/")
public class SearchController {

	private final SearchService searchService;

	private static final String GET_SEARCH_WORD = "search/word";

	@GetMapping(GET_SEARCH_WORD)
	public Response getSearchWord(@PageableDefault(size = 6) Pageable pageable) {
		return new Response(searchService.findTopWords(pageable));
	}

}
