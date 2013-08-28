package net.kaoriya.qb.qdigest_benchmark;

import java.util.Random;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import net.kaoriya.qb.redis_qdigest.QDigest;

public class Main
{
    public static void insertValues(IQDigest qd, long seed, int count)
        throws Exception
    {
        Watch w = new Watch("insert values: count=" + count);
        Random r = new Random(seed);
        for (int i = 0; i < count; ++i) {
            qd.offer(r.nextInt(1000000));
        }
        w.stop();
    }

    public static void queryQuantiles(IQDigest qd, long seed, int count)
        throws Exception
    {
        Watch w = new Watch("query quantiles: count=" + count);
        Random r = new Random(seed);
        for (int i = 0; i < count; ++i) {
            qd.quantile(r.nextFloat());
        }
        w.stop();
    }

    public static void benchCommons(IQDigestFactory factory)
        throws Exception
    {
        IQDigest qd = factory.newInstance(100);
        insertValues(qd, 0, 1000);
        queryQuantiles(qd, 0, 1000);
    }

    public static void benchmarkDummyQDigest()
    {
        System.out.println("benchmark(dummy)");
        DummyQDigestFactory factory = new DummyQDigestFactory();
        try {
            benchCommons(factory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public static void benchmarkMetricsHistogram()
    {
        System.out.println("benchmark(metrics.histogram)");
        MetricsHistogramFactory factory = new MetricsHistogramFactory();
        try {
            benchCommons(factory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void executeBenchmark(String host) {
        System.out.println("");
        benchmarkDummyQDigest();
        System.out.println("");
        benchmarkRedisQDigest(host);
        System.out.println("");
        benchmarkStreamQDigest();
        System.out.println("");
        benchmarkMetricsHistogram();
    }

    public static void main(String[] args) {
        executeBenchmark("127.0.0.1");
    }
}
