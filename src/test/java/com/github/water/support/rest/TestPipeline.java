package com.github.water.support.rest;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * @author weiwei.Wang
 * @date 2016年6月15日
 * @todo TODO 2016年6月15日
 */
public class TestPipeline {

	private Jedis redis;

	@Before
	public void setup() {
		// 连接redis服务器，192.168.0.100:6379
		redis = new Jedis("172.16.40.104", 6379);
	}

	@Test
	public void testAdd1() {
		Map<String, String> data = new HashMap<String, String>();
		long start = System.currentTimeMillis();
		// 直接hmset
		for (int i = 0; i < 2; i++) {
			data.clear();
			data.put("111_k_" + i, "v_" + i);
			redis.hmset("111_key_" + i, data);
		}
		long end = System.currentTimeMillis();
		System.out.println("dbsize:[" + redis.dbSize() + "] .. ");
		System.out.println("hmset without pipeline used [" + (end - start)
				 + "] seconds ..");

	}

	@Test
	public void testAdd2() {
		Map<String, String> data = new HashMap<String, String>();
		long start = System.currentTimeMillis();
		// 使用pipeline hmset
		Pipeline p = redis.pipelined();
		start = System.currentTimeMillis();
		for (int i = 0; i < 2; i++) {
			data.clear();
			data.put("222_k_" + i, "v_" + i);
			p.hmset("222_key_" + i, data);
		}
		p.sync();
		Long end = System.currentTimeMillis();
		System.out.println("dbsize:[" + redis.dbSize() + "] .. ");
		System.out.println("hmset with pipeline used [" + (end - start)
				+ "] seconds ..");
	}

//	@Test
//	public void testGet3() {
//		long start = System.currentTimeMillis();
//		// hmget
//		Set<String> keys = redis.keys("*");
//		// 直接使用Jedis hgetall
//		start = System.currentTimeMillis();
//		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
//		for (String key : keys) {
//			result.put(key, redis.hgetAll(key));
//		}
//		long end = System.currentTimeMillis();
//		System.out.println("result size:[" + result.size() + "] ..");
//		System.out.println("hgetAll without pipeline used [" + (end - start)
//				 + "] seconds ..");
//	}

//	@Test
//	public void testAdd4() {
//		// 使用pipeline hgetall
//		Map<String, Response<Map<String, String>>> responses = new HashMap<String, Response<Map<String, String>>>(
//				keys.size());
//		result.clear();
//		start = System.currentTimeMillis();
//		for (String key : keys) {
//			responses.put(key, p.hgetAll(key));
//		}
//		p.sync();
//		for (String k : responses.keySet()) {
//			result.put(k, responses.get(k).get());
//		}
//		end = System.currentTimeMillis();
//		System.out.println("result size:[" + result.size() + "] ..");
//		System.out.println("hgetAll with pipeline used [" + (end - start)
//				/ 1000 + "] seconds ..");
//	}


}
