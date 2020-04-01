package rs.raf.word_distribution;

import rs.raf.word_distribution.counter_cruncher.BagOfWords;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiFunction;

public abstract class Output<K, V> implements Runnable {
    protected BlockingQueue<CruncherDataFrame<K, V>> cruncherDataFrameBlockingQueue;

    public Output() {
        this.cruncherDataFrameBlockingQueue = new LinkedBlockingQueue<>();
    }

    public abstract void process(CruncherDataFrame<K, V> cruncherDataFrame);

    public abstract void store(String name, Future<Map<K, V>> future);

    public abstract Map<K, V> take(String name);

    public abstract Map<K, V> poll(String name);

    public abstract void aggregate(String newName, List<String> existingResults, BiFunction<V, V, V> aggregatingFunction);

    public void putCruncherDataFrame(CruncherDataFrame<K, V> cruncherDataFrame) {
        this.cruncherDataFrameBlockingQueue.add(cruncherDataFrame);
    }

    @Override
    public void run() {
        while (true) {
            try {
                CruncherDataFrame<K, V> cruncherDataFrame = this.cruncherDataFrameBlockingQueue.take();

                this.process(cruncherDataFrame);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
