package com.github.water.service.persondir.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;

import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.AbstractTicketRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.water.service.RedisService;

public class RedisTicketRegistry extends AbstractTicketRegistry{

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static String prefix;
	private static int st_time; // ST最大空閑時間
	private static int tgt_time; // TGT最大空閑時間
	
	private static RedisService redisService;

	public static RedisService getRedisService() {
		return redisService; 
	}

	public static void setRedisService(RedisService redisService) {
		RedisTicketRegistry.redisService = redisService;
	}

	public static String getPrefix() {
		return prefix;
	}

	public static void setPrefix(String prefix) {
		RedisTicketRegistry.prefix = prefix;
	}


	public static void setSt_time(int st_time) {
		RedisTicketRegistry.st_time = st_time;
	}

	public static void setTgt_time(int tgt_time) {
		RedisTicketRegistry.tgt_time = tgt_time;
	}


	@Override
	public void addTicket(Ticket ticket) {
		long start = System.currentTimeMillis();
		int seconds = 0;
		String key = prefix + ticket.getId();
		if (ticket instanceof TicketGrantingTicket) {
			seconds = tgt_time;
		} else {
			seconds = st_time ;
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(ticket);

		} catch (Exception e) {
			logger.error("adding ticket to redis error.");
		} finally {
			try {
				if (null != oos)
					oos.close();
			} catch (Exception e) {
				logger.error("oos closing error when adding ticket to redis.");
			}
		}
//	批量插入	
		// Map<byte[], byte[]> data = new HashMap<byte[], byte[]>();
		// for (int i = 0; i < 10000; i++) {
		// data.clear();
		// data.put(key.getBytes(), bos.toByteArray());
		// }
		// redisService.addHcode(data,seconds);
		redisService.set(key.getBytes(), bos.toByteArray(), seconds);
		
		long end = System.currentTimeMillis(); // 获取结束时间
		System.err.println("addTicket~~~~~~： " + (end - start)
				+ "ms");
	}
	
//	@Override
//	public void addTicket(Ticket ticket) {
//		Map<byte[], byte[]> data = new HashMap<byte[], byte[]>();
//		
//		cachePool = new JedisPool(new JedisPoolConfig(), hosts, port);
//		Jedis jedis = cachePool.getResource();
//		Pipeline p = jedis.pipelined();
//		int seconds = 0;
//		String key = prefix + ticket.getId();
//		System.err.println(("Ticket [" + ticket.getId()+ " is of type " + ticket.getClass()));
//		if (ticket instanceof TicketGrantingTicket) {
//			seconds = tgt_time;
//		} else {
//			seconds = st_time ;
//		}
//
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		ObjectOutputStream oos = null;
//		try {
//			oos = new ObjectOutputStream(bos);
//			oos.writeObject(ticket);
//
//		} catch (Exception e) {
//			logger.error("adding ticket to redis error.");
//		} finally {
//			try {
//				if (null != oos)
//					oos.close();
//			} catch (Exception e) {
//				logger.error("oos closing error when adding ticket to redis.");
//			}
//		}
//		
//		for (int i = 0; i < 10000; i++) {
//			data.clear();
//			data.put(key.getBytes(), bos.toByteArray());
//			p.hmset(key.getBytes(), data);
//			p.expire(key.getBytes(), seconds);
//		}
//		
//		p.sync();
//		
////		jedis.set(key.getBytes(), bos.toByteArray());
////		jedis.expire(key.getBytes(), seconds);
//
//		cachePool.returnResource(jedis);
//
//	}

	@Override
	public Ticket getTicket(final String ticketId) {
		return getRawTicket(ticketId);
	}

	/**
     * Retrieve a ticket from the registry.
     *
     * @param ticketId the id of the ticket we wish to retrieve
     * @return the requested ticket.
     */
	private Ticket getRawTicket(final String ticketId) {

		if (null == ticketId)
			return null;
		String newticketId = prefix + ticketId;
		Ticket ticket = null;
		byte[] value = redisService.get(newticketId.getBytes());
		if (null == value) {
			return null;
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(value);
		ObjectInputStream ois = null;

		try {
			ois = new ObjectInputStream(bais);
			ticket = (Ticket) ois.readObject();
		} catch (Exception e) {
			logger.error("getting ticket to redis error.");
		} finally {
			try {
				if (null != ois)
					ois.close();
			} catch (Exception e) {
				logger.error("ois closing error when getting ticket to redis.");
			}
		}
		return ticket;
	}

	@Override
	public boolean deleteTicket(final String ticketId) {
		if (ticketId == null) {
			return false;
		}
		String newticketId = prefix + ticketId;
		redisService.del(newticketId.getBytes());
		return true;
	}

	@Override
	public Collection<Ticket> getTickets() {

		throw new UnsupportedOperationException("GetTickets not supported.");

	}

	protected boolean needsCallback() {
		return false;
	}

	protected void updateTicket(final Ticket ticket) {
		addTicket(ticket);
	}


}
