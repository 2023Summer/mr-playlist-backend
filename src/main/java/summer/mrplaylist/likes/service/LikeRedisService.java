package summer.mrplaylist.likes.service;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeRedisService {
	private final RedisTemplate<String, Object> redisTemplate;

	public void setData(String key, Object value) {
		redisTemplate.opsForSet().add(key, value);
	}

	public void deleteData(String key, Object value) {
		redisTemplate.opsForSet().remove(key, value);
	}

	public Set<Object> getAllData(String key) {
		return redisTemplate.opsForSet().members(key);
	}
}
