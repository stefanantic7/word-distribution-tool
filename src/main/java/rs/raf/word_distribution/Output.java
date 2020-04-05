package rs.raf.word_distribution;

import rs.raf.word_distribution.counter_cruncher.BagOfWords;

import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class Output<K, V> implements Runnable {
    protected BlockingQueue<Optional<CruncherDataFrame<K, V>>> cruncherDataFrameBlockingQueue;

    public Output() {
        this.cruncherDataFrameBlockingQueue = new LinkedBlockingQueue<>();
    }

    public abstract void process(CruncherDataFrame<K, V> cruncherDataFrame);

    public abstract void store(String name, Future<Map<K, V>> future);

    public abstract Map<K, V> take(String name);

    public abstract Map<K, V> poll(String name);

    public abstract void aggregate(String newName, List<String> existingResults, BiFunction<V, V, V> aggregatingFunction, Function<String, Void> itemProcessedCallback);

    public void putCruncherDataFrame(CruncherDataFrame<K, V> cruncherDataFrame) {
        this.cruncherDataFrameBlockingQueue.add(Optional.of(cruncherDataFrame));
    }

    @Override
    public void run() {
        while (true) {
            try {
                Optional<CruncherDataFrame<K, V>> cruncherDataFrameOptional = this.cruncherDataFrameBlockingQueue.take();
                if(cruncherDataFrameOptional.isEmpty()) {
                    break;
                }
                CruncherDataFrame<K, V> cruncherDataFrame = cruncherDataFrameOptional.get();

                this.process(cruncherDataFrame);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void destroy() {
        try {
            this.cruncherDataFrameBlockingQueue.put(Optional.ofNullable(null));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
