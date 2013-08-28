package net.kaoriya.qb.qdigest_benchmark;

public final class MetricsHistogramFactory implements IQDigestFactory
{
    public static class MetricsHistogram implements IQDigest
    {
        public MetricsHistogram() {
        }

        public void offer(long value) {
        }

        public long quantile(float q) {
            return 0L;
        }
    }

    public IQDigest newInstance(long factor)
    {
        return new MetricsHistogram();
    }

}
