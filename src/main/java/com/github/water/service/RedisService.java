package com.github.water.service;

import java.util.Map;

/**
 * @author weiwei.Wang
 * redis 的操作开放接口 
 */

public interface RedisService {

	/**
	 * 通过key删除
	 * 
	 * @param key
	 */
	public abstract long del(String... keys);
	
	/**
	 * 通过key删除
	 * 
	 * @param key
	 */
	public abstract long del(byte[] keys);

	/**
	 * 添加key value 并且设置存活时间(byte)
	 * 
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	public abstract void set(byte[] key, byte[] value, long liveTime);
	
	/**
	 * 批量 添加key value 并且设置存活时间(byte)
	 * 
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	public abstract void addHcode(Map<byte[], byte[]> map, long liveTime);

	/**
	 * 添加key value 并且设置存活时间
	 * 
	 * @param key
	 * @param value
	 * @param liveTime
	 *            单位秒
	 */
	public abstract void set(String key, String value, long liveTime);

	/**
	 * 添加key value
	 * 
	 * @param key
	 * @param value
	 */
	public abstract void set(String key, String value);

	/**
	 * 添加key value (字节)(序列化)
	 * 
	 * @param key
	 * @param value
	 */
	public abstract void set(byte[] key, byte[] value);

	/**
	 * 获取redis value (byte[])
	 * 
	 * @param key
	 * @return
	 */
	public abstract byte[] get(byte[] key);
	
	/**
	 * 获取redis value (String)
	 * 
	 * @param key
	 * @return
	 */
	public abstract String get(String key);


	/**
	 * 检查key是否已经存在
	 * 
	 * @param key
	 * @return
	 */
	public abstract boolean exists(String key);

	/**
	 * 清空redis 所有数据
	 * 
	 * @return
	 */
	public abstract String flushDB();

	/**
	 * 查看redis里有多少数据
	 */
	public abstract long dbSize();

	/**
	 * 检查是否连接成功
	 * 
	 * @return
	 */
	public abstract String ping();

}
