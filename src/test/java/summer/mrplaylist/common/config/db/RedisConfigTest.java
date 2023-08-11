package summer.mrplaylist.common.config.db;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class RedisConfigTest {
	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Test
	public void redisTest() throws Exception {
		String email = "test@naver.com";
		String value = "test";
		redisTemplate.opsForValue().set(email, value);

		Assertions.assertThat(value).isEqualTo(redisTemplate.opsForValue().get(email));
	}

}

