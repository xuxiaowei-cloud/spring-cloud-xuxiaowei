package cloud.xuxiaowei.file.redis;

import cloud.xuxiaowei.redis.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link StringRedisTemplate} 测试类
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@SpringBootTest
public class StringRedisTemplateOpsForHashTests {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Test
	void start() {
		// 产生一个随机 key
		String key = RandomStringUtils.randomAlphabetic(4);
		// Map 中的 key
		String hashKey = "uuid";
		// 作者
		String author = "徐晓伟";

		// 根据 key 获取 Redis 中的一个 Map 类型中的一个键是 hashKey 的 value
		Object value = stringRedisTemplate.opsForHash().get(key, hashKey);
		// value 为空
		assertNull(value);

		// 创建一个 Map
		Map<String, Object> map = new HashMap<>();
		// 产生一个随机数
		String uuid = UUID.randomUUID().toString();
		// 将随机数放入 Map
		map.put(hashKey, uuid);
		map.put("name", author);
		// 将 Map 放入 Redis 中
		stringRedisTemplate.opsForHash().putAll(key, map);

		// 根据 key 获取 Redis 中的一个 Map 类型中的一个键是 hashKey 的 value
		value = stringRedisTemplate.opsForHash().get(key, hashKey);
		// Redis 中 键名是 key 的 Map 中 uuid 的 value 与 随机数相同
		assertEquals(value, uuid);

		// 获取过期时间
		Long time = stringRedisTemplate.getExpire(key);
		// 未设置过期时间，值为 -1
		assertEquals(time, -1);

		// 定义过期时间
		long timeout = 80;
		TimeUnit unit = TimeUnit.SECONDS;

		// 设置过期时间
		Boolean expire = stringRedisTemplate.expire(key, timeout, unit);
		// 设置结果
		assertNotNull(expire);
		assertTrue(expire);

		// 获取 Redis 中 key 的过期时间
		time = stringRedisTemplate.getExpire(key);

		// 比较过期时间
		assertEquals(timeout, time);

		String redisVersion = RedisUtils.redisVersion(stringRedisTemplate);
		assertNotNull(redisVersion);

		// 删除 Redis 中的 key
		Long delete = stringRedisTemplate.opsForHash().delete(key, hashKey);
		// 删除结果
		assertNotNull(delete);
		assertEquals(delete, 1);

		// 判断是否存在
		Boolean hassedKey = stringRedisTemplate.opsForHash().hasKey(key, hashKey);
		assertNotNull(hassedKey);
		assertFalse(hassedKey);

		// 根据 key 获取 Redis 中的一个 Map 类型中的一个键是 hashKey 的 value
		value = stringRedisTemplate.opsForHash().get(key, hashKey);
		// 结果为 null
		assertNull(value);

		// 放入一个值
		String id = UUID.randomUUID().toString();
		stringRedisTemplate.opsForHash().put(key, "id", id);
		Object object = stringRedisTemplate.opsForHash().get(key, "id");
		assertNotNull(object);
		assertEquals(object, id);

		// 将一个 Map 补充到已在 Redis 经存在的 key 中
		Map<String, Object> addMap = new HashMap<>();
		addMap.put("a", "张三");
		addMap.put("b", "李四");
		stringRedisTemplate.opsForHash().putAll(key, addMap);
		Boolean hassedAKey = stringRedisTemplate.opsForHash().hasKey(key, "a");
		Boolean hassedBKey = stringRedisTemplate.opsForHash().hasKey(key, "b");
		Boolean hassedNameKey = stringRedisTemplate.opsForHash().hasKey(key, "name");
		assertNotNull(hassedAKey);
		assertTrue(hassedAKey);
		assertNotNull(hassedBKey);
		assertTrue(hassedBKey);
		assertNotNull(hassedNameKey);
		assertTrue(hassedNameKey);

		// 获取 Map
		Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(key);
		log.info("entries:\n{}", entries);
		assertNotNull(entries.get("name"));
		assertNotNull(entries.get("id"));
		assertNotNull(entries.get("a"));
		assertNotNull(entries.get("b"));

		// 获取 Map 中的 key
		Set<Object> keys = stringRedisTemplate.opsForHash().keys(key);
		log.info("keys:\n{}", keys);
		assertTrue(keys.contains("name"));
		assertTrue(keys.contains("id"));
		assertTrue(keys.contains("a"));
		assertTrue(keys.contains("b"));
		assertEquals(keys.size(), 4);

		// Redis Map 计算 long 类型
		int i1 = 6, i2 = 3, i3 = i1 + i2;
		stringRedisTemplate.opsForHash().put(key, "i", i1 + "");
		Long i4 = stringRedisTemplate.opsForHash().increment(key, "i", i2);
		assertNotNull(i4);
		assertEquals(i3, i4);

		// Redis Map 计算 double 类型
		double d1 = 0.2D, d2 = 0.3D, d3 = d1 + d2;
		stringRedisTemplate.opsForHash().put(key, "d", d1 + "");
		Double d4 = stringRedisTemplate.opsForHash().increment(key, "d", d2);
		assertNotNull(d4);
		assertEquals(d3, d4);

		// 根据 key 获取 Redis 中的一个 Map 类型中的一个键是 name 的 value
		Object name = stringRedisTemplate.opsForHash().get(key, "name");
		// 结果
		assertEquals(name, author);
	}

}
