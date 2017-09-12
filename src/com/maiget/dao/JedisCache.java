package com.maiget.dao;

import redis.clients.jedis.Jedis;

public class JedisCache {
	
	public static Jedis getJedis(String host){
		Jedis jedis =null;
	
		try{
			jedis = new Jedis(host);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Jedis拒绝访问："+e);
		}
		//连接成功 则是PONG
		if("PONG".equals(jedis.ping())){
			System.out.println("获得Jedis对象");
			return jedis;
		}
		System.out.println("未连接");
		return null;
	}
}
