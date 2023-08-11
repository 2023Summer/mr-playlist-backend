package summer.mrplaylist.common.service;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {
	private final StringRedisTemplate template;

	public String getData(String key) {
		ValueOperations<String, String> valueOperations = template.opsForValue();
		return valueOperations.get(key);
	}

	public boolean existData(String key) {
		return Boolean.TRUE.equals(template.hasKey(key));
	}

	public void setData(String key, String value) {
		template.opsForValue().set(key, value);
	}

	public void setDataWithExpire(String key, String value, long duration) {
		Duration expireDuration = Duration.ofSeconds(duration);
		template.opsForValue().set(key, value, expireDuration);
	}

	public void deleteData(String key) {
		template.delete(key);
	}

}