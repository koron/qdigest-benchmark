package net.kaoriya.qb.qdigest_benchmark;

import net.kaoriya.qb.slotted_histogram.SlottedHistogram;
import net.kaoriya.qb.slotted_histogram.Splits;

public final class SlottedHistogramFactory implements IQDigestFactory
{
    public static class QDigest implements IQDigest
    {
        final SlottedHistogram histogram;

        public QDigest(SlottedHistogram histogram) {
            this.histogram = histogram;
        }

        public void offer(long value) {
            this.histogram.offer(value);
        }

        public long quantile(float q) {
            return this.histogram.quantile(q);
        }
    }

    public IQDigest newInstance(long factor)
    {
        long[] data = new long[(int)factor];
        for (int i = 0; i < factor; ++i) {
            data[i] = i * 1000000L;
        }
        Splits splits = Splits.newInstance(data);
        return new QDigest(new SlottedHistogram(splits));
    }

}
