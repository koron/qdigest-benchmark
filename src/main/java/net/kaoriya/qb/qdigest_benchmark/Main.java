package net.kaoriya.qb.qdigest_benchmark;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import net.kaoriya.qb.redis_qdigest.QDigest;

public class Main
{
    public static void benchmark(Jedis jedis) throws Exception
    {
        System.out.println("benchmark(redis-qdigest)");
        QDigest.dropInstance(jedis, "foo");
        QDigest qd = QDigest.getInstance(jedis, "foo", 20);
        qd.offer(10);
        qd.offer(20, 30, 40, 50);
        System.out.println("qd(0.0f)=" + qd.quantile(0.0f));
        System.out.println("qd(0.5f)=" + qd.quantile(0.5f));
        System.out.println("qd(1.0f)=" + qd.quantile(1.0f));
    }

    public static void benchmarkRedisQDigest(String host)
    {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), host);
        Jedis jedis = pool.getResource();
        try {
            benchmark(jedis);
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
        com.clearspring.analytics.stream.quantile.QDigest qd =
            new com.clearspring.analytics.stream.quantile.QDigest(20);
        qd.offer(10);
        qd.offer(20);
        qd.offer(30);
        qd.offer(40);
        qd.offer(50);
        System.out.println("qd(0.0f)=" + qd.getQuantile(0.0f));
        System.out.println("qd(0.5f)=" + qd.getQuantile(0.5f));
        System.out.println("qd(1.0f)=" + qd.getQuantile(1.0f));
    }

    public static void executeBenchmark(String host) {
        benchmarkRedisQDigest(host);
        benchmarkStreamQDigest();
    }

    public static void main(String[] args) {
        executeBenchmark("127.0.0.1");
    }
}
