package summer.mrplaylist.search.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.common.constant.RedisConstants;
import summer.mrplaylist.search.dto.SearchCond;
import summer.mrplaylist.search.dto.Topic;

@Slf4j
@Transactional
@SpringBootTest
class SearchServiceTest {

	@Autowired
	SearchService searchService;

	@Autowired
	StringRedisTemplate redisTemplate;

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

	@DisplayName("검색 기능 <Category> 테스트")
	@Test
	public void testSearch() throws Exception {
		//given
		SearchCond searchCond = new SearchCond("몽환", Topic.CATEGORY);
		PageRequest pageRequest = PageRequest.of(0, 2);
		//when
		Page<?> resultList = searchService.search(searchCond, pageRequest);
		//then
		log.info("Search result = {}", resultList.getContent());

		assertThat(resultList.getContent()).isNotNull();
	}

	// @PostConstruct
	// public void initRedis() {
	// 	redisTemplate.delete(RedisConstants.SEARCH);
	// }

	@Test
	@DisplayName("자주 검색한 키워드 캐싱")
	public void saveRedisRank() throws Exception {
		//given
		SearchCond searchCond = new SearchCond("킹누", Topic.PLAYLIST);
		PageRequest pageRequest = PageRequest.of(0, 2);
		//when
		searchService.search(searchCond, pageRequest);
		for (int i = 0; i < 10; i++) {
			searchService.search(searchCond, pageRequest);
		}

		searchCond = new SearchCond("테스트", Topic.PLAYLIST);
		searchService.search(searchCond, pageRequest);
		for (int i = 0; i < 5; i++) {
			searchService.search(searchCond, pageRequest);
		}
		//then
		ZSetOperations<String, String> zSet =
			redisTemplate.opsForZSet();

		Set<ZSetOperations.TypedTuple<String>> searchWord = zSet.reverseRangeWithScores(RedisConstants.SEARCH, 0, 9);
		log.info("Search Ranking = {}", searchWord.stream().toList());

		assertThat(searchWord.stream().toList().get(0).getValue())
			.isEqualTo("킹누");
	}

}
