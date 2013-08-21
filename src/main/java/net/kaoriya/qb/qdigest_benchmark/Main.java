package net.kaoriya.qb.qdigest_benchmark;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import net.kaoriya.qb.redis_qdigest.QDigest;

public class Main
{
    public static void benchmark(Jedis jedis) throws Exception
    {
        System.out.println("benchmark(Jedis)");
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

    public static void executeBenchmark(String host) {
        benchmarkRedisQDigest(host);
    }

    public static void main(String[] args) {
        executeBenchmark("127.0.0.1");
    }
}
