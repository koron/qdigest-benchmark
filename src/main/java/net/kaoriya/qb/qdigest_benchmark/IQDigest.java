package net.kaoriya.qb.qdigest_benchmark;

public interface IQDigest
{
    void offer(long value) throws Exception;
    long quantile(float q) throws Exception;
}
