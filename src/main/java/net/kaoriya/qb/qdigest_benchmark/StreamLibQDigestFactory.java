package net.kaoriya.qb.qdigest_benchmark;

import com.clearspring.analytics.stream.quantile.QDigest;

public final class StreamLibQDigestFactory implements IQDigestFactory
{
    public static class StreamLibQDigest implements IQDigest
    {
        final QDigest qd;

        public StreamLibQDigest(QDigest qd) {
            this.qd = qd;
        }

        public void offer(long value) {
            this.qd.offer(value);
        }

        public long quantile(float q) {
            return this.qd.getQuantile(q);
        }
    }

    public IQDigest newInstance(long factor)
    {
        QDigest qd = new QDigest(factor);
        return new StreamLibQDigest(qd);
    }

}
