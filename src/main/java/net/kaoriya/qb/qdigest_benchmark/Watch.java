package net.kaoriya.qb.qdigest_benchmark;

public class Watch
{
    private final String message;
    private long startTime;
    private long accumulateTime = 0;

    public Watch(String message) {
        this.message = message;
        start();
    }

    public void stop() {
        if (this.startTime >= 0) {
            split();
        }
        System.out.println(String.format(
                    "[%2$12.6fs] %1$s",
                    this.message,
                    this.accumulateTime * 0.000000001));
    }

    public void start() {
        this.startTime = System.nanoTime();
    }

    public void split() {
        this.accumulateTime += System.nanoTime() - this.startTime;
        this.startTime = -1;
    }

    public long getAccumulateTime() {
        return this.accumulateTime;
    }
}
