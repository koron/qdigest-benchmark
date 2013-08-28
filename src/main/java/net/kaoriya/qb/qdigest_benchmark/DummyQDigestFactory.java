package net.kaoriya.qb.qdigest_benchmark;

public final class DummyQDigestFactory implements IQDigestFactory
{
    public static class DummyQDigest implements IQDigest
    {
        public DummyQDigest() {
        }

        public void offer(long value) {
        }

        public long quantile(float q) {
            return 0L;
        }
    }

    public IQDigest newInstance(long factor)
    {
        return new DummyQDigest();
    }

}
