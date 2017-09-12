package test;

import redis.clients.jedis.Jedis;

public class RedisTest {
	public static void main(String[] args){
		Jedis jedis = new Jedis("localhost");
		String str = "1";
		System.out.println("链接成功 "+ jedis.ping() );
		jedis.sadd("title", "url1");
		jedis.sadd("title", "url2");
		jedis.sadd("title", "url3");
		jedis.sadd("title", "url4");
		jedis.sadd("title", "url5");
		if(!jedis.sismember("title", str)){
			jedis.sadd("title", str);
		}else{
			System.out.println("重复");
		}
		jedis = new Jedis("localhost");
		System.out.println(jedis.smembers("title"));
		
	}
}
