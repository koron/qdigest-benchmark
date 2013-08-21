package net.kaoriya.qb.qdigest_benchmark;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import net.kaoriya.qb.redis_qdigest.QDigest;

public class Main
{
    public static void benchCommons(IQDigestFactory factory)
        throws Exception
    {
        IQDigest qd = factory.newInstance(20);
        qd.offer(10);
        qd.offer(20);
        qd.offer(30);
        qd.offer(40);
        qd.offer(50);
        System.out.println("qd(0.0f)=" + qd.quantile(0.0f));
        System.out.println("qd(0.5f)=" + qd.quantile(0.5f));
        System.out.println("qd(1.0f)=" + qd.quantile(1.0f));
    }

    public static void benchJedis(Jedis jedis) throws Exception
    {
        // TODO:
    }

    public static void benchmarkRedisQDigest(String host)
    {
        System.out.println("benchmark(redis-qdigest)");
        JedisPool pool = new JedisPool(new JedisPoolConfig(), host);
        Jedis jedis = pool.getResource();
        try {
            RedisQDigestFactory factory = new RedisQDigestFactory(jedis);
            benchCommons(factory);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            pool.returnResource(jedis);
        }
        pool.destroy();
    }

    public static void benchmarkStreamQDigest()
    {
        System.out.println("benchmark(stream-lib)");
        StreamLibQDigestFactory factory = new StreamLibQDigestFactory();
        try {
            benchCommons(factory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void executeBenchmark(String host) {
        System.out.println("");
        benchmarkRedisQDigest(host);
        System.out.println("");
        benchmarkStreamQDigest();
    }

    public static void main(String[] args) {
        executeBenchmark("127.0.0.1");
    }
}
