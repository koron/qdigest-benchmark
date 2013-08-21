package net.kaoriya.qb.qdigest_benchmark;

public interface IQDigestFactory
{
    IQDigest newInstance(long factor) throws Exception;
}
