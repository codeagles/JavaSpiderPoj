package com.maiget.dao;

import redis.clients.jedis.Jedis;

public class JedisCache {
	
	public static Jedis getJedis(String host){
		
		Jedis jedis = new Jedis(host);
		//连接成功 则是PONG
		if("PONG".equals(jedis.ping())){
			return jedis;
		}
		return null;
	}
}
