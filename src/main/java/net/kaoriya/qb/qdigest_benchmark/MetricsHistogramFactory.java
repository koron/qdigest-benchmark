package net.kaoriya.qb.qdigest_benchmark;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.Reservoir;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.UniformReservoir;

public final class MetricsHistogramFactory implements IQDigestFactory
{
    public static class MetricsHistogram implements IQDigest
    {
        final Reservoir reservoir;
        final Histogram histogram;

        public MetricsHistogram(Reservoir reservoir) {
            this.reservoir = reservoir;
            this.histogram = new Histogram(reservoir);
        }

        public void offer(long value) {
            this.histogram.update(value);
        }

        public long quantile(float q) {
            Snapshot snapshot = this.histogram.getSnapshot();
            return (long)snapshot.getValue(q);
        }
    }

    public IQDigest newInstance(long factor)
    {
        return new MetricsHistogram(new UniformReservoir());
    }
}
