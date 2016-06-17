package com.github.water.service;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author weiwei.Wang
 * @date 2016年6月15日
 * @todo TODO 2016年6月15日
 */
@Service(value = "redisService")
public class RedisServiceImpl implements RedisService {

	private static String redisCode = "utf-8";

	private RedisTemplate<Object, Object> redisTemplate;

	public RedisTemplate<Object, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * @param key
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public long del(final String... keys) {
		return (long) redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				long result = 0;
				for (int i = 0; i < keys.length; i++) {
					result = connection.del(keys[i].getBytes());
				}
				return result;
			}
		});
	}
	
	
	/**
	 * @param key
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public long del(final byte[] keys) {
		return (long) redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				long result = 0;
//				for (int i = 0; i < keys.length; i++) {
//					result = connection.del(keys[i].getBytes());
//				}
				result = connection.del(keys);
				return result;
			}
		});
	}

	/**
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void set(final byte[] key, final byte[] value, final long liveTime) {
		redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.set(key, value);
				if (liveTime > 0) {
					connection.expire(key, liveTime);
				}
				return 1L;
			}
		});
	}

	/**
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	public void set(String key, String value, long liveTime) {
		this.set(key.getBytes(), value.getBytes(), liveTime);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		this.set(key, value, 0L);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void set(byte[] key, byte[] value) {
		this.set(key, value, 0L);
	}

	/**
	 * @param key
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String get(final String key) {
		return (String) redisTemplate.execute(new RedisCallback() {
			public String doInRedis(RedisConnection connection)
					throws DataAccessException {
				try {
					return new String(connection.get(key.getBytes()), redisCode);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return "";
			}
		});
	}
	
	/**
	 * @param key
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public byte[] get(final byte[] key) {
		return (byte[]) redisTemplate.execute(new RedisCallback() {
			public byte[] doInRedis(RedisConnection connection)
					throws DataAccessException {
				try {
//					return new String(connection.get(key), redisCode);
					return connection.get(key);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}

	/**
	 * @param key
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean exists(final String key) {
		return (boolean) redisTemplate.execute(new RedisCallback() {
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.exists(key.getBytes());
			}
		});
	}

	/**
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String flushDB() {
		return (String) redisTemplate.execute(new RedisCallback() {
			public String doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.flushDb();
				return "ok";
			}
		});
	}

	/**
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public long dbSize() {
		return (long) redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.dbSize();
			}
		});
	}

	/**
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String ping() {
		return (String) redisTemplate.execute(new RedisCallback() {
			public String doInRedis(RedisConnection connection)
					throws DataAccessException {

				return connection.ping();
			}
		});
	}

	private RedisServiceImpl() {

	}

	/**
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addHcode(final Map<byte[], byte[]> map, final long liveTime) {
		redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
					byte[] key = entry.getKey();
					byte[] name = entry.getValue();
					connection.setNX(key, name);
					if (liveTime > 0) {
						connection.expire(key, liveTime);
					}
				}
				return 1L;
			}
		});
	}

}
