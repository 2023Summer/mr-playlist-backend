package summer.mrplaylist.likes.service;

import java.util.Objects;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikesRedisService {
	private final RedisTemplate<String, Long> redisTemplate;
	@Resource(name = "redisStringLongTemplate")
	private SetOperations<String, Long> setOperations;

	public void setData(String key, Long value) {
		setOperations.add(key, value);
	}

	public void deleteData(String key, Long value) {
		setOperations.remove(key, value);
	}

	public Set<Long> getAllData(String key) {
		return setOperations.members(key);
	}

	public Set<String> getKeys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	public void flushAll() {
		Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().serverCommands().flushAll();
	}
}
