package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * created by Christophe
 * 2018/5/1 下午7:00
 */
public class RedisPool  {
    private static JedisPool pool;     //jedis连接池

    //最大连接数
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));

    //在jedispool中最大的(idle)空闲状态的jedis实例个数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));

    //在jedispool中最小的(idle)空闲状态的jedis实例个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "2"));

    //在borrow一个jedis实例的时候，是否要进行验证操作。如果赋值为true，则得到的实例肯定是可以用的；
    private static boolean testOnBorrow =  Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));

    //在还一个jedis实例的时候，是否要进行验证操作。如果赋值为true，则归还jedispool的实例肯定是可以用的；
    private static boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));

    private static String redisIp = PropertiesUtil.getProperty("redis.ip");
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));

    private static void initPool(){
        //这个config只用一个实例就可以
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        config.setBlockWhenExhausted(true); //连接耗尽时是否阻塞，false：抛异常，true：会阻塞直到超时，默认true

        pool = new JedisPool(config, redisIp, redisPort, 1000*2);
    }

    //只实例化一次
    static {
        initPool();
    }

    /**
     * 从连接池中获取一个jedis实例
     * @return
     */
    public static Jedis getJedis(){
        return pool.getResource();
    }

    /**
     * 放回坏的连接
     * @param jedis
     */
    public static void returnBrokenResource(Jedis jedis){
        pool.returnBrokenResource(jedis);
    }

    /**
     * 把jedis放回连接池
     * @param jedis
     */
    public static void returnResource(Jedis jedis){
        pool.returnResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("lyukey", "lyuValue");

        pool.returnResource(jedis);
        pool.destroy();  //临时测试用
        System.out.println("添加完毕");
    }
}
