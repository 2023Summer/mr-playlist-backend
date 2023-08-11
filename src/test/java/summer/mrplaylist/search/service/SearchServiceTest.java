package summer.mrplaylist.search.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.search.dto.SearchCond;
import summer.mrplaylist.search.dto.Topic;

@Slf4j
@Transactional
@SpringBootTest
class SearchServiceTest {

	@Autowired
	SearchService searchService;

	@DisplayName("검색 기능 테스트")
	@ParameterizedTest
	@EnumSource(value = Topic.class, names = {"PLAYLIST", "MUSIC", "ARTIST"})
	public void search(Topic topic) throws Exception {
		//given
		SearchCond searchCond = new SearchCond("킹누", topic);
		PageRequest pageRequest = PageRequest.of(0, 2);
		//when
		Page<?> resultList = searchService.search(searchCond, pageRequest);
		//then
		log.info("Search result = {}", resultList.getContent());

		assertThat(resultList.getContent()).isNotNull();
	}

}
