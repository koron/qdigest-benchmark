package net.kaoriya.qb.qdigest_benchmark;

import redis.clients.jedis.Jedis;

import net.kaoriya.qb.redis_qdigest.QDigest;

public final class RedisQDigestFactory implements IQDigestFactory
{
    public static class RedisQDigest implements IQDigest
    {
        final QDigest qd;

        public RedisQDigest(QDigest qd) {
            this.qd = qd;
        }

        public void offer(long value) throws Exception {
            this.qd.offer(value);
        }

        public long quantile(float q) throws Exception {
            return this.qd.quantile(q);
        }
    }

    private final Jedis jedis;
    private int count = 0;

    public RedisQDigestFactory(Jedis jedis)
    {
        this.jedis = jedis;
    }

    private synchronized String newName()
    {
        String name = "qdbench_" + this.count;
        ++this.count;
        return name;
    }

    public IQDigest newInstance(long factor) throws Exception
    {
        String name = newName();
        QDigest.dropInstance(this.jedis, name);
        QDigest qd = QDigest.getInstance(this.jedis, name, factor);
        return new RedisQDigest(qd);
    }
}
