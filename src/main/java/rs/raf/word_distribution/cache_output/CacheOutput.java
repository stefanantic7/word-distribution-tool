package rs.raf.word_distribution.cache_output;

import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.Output;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CacheOutput<K, V> extends Output<K, V> {

    private Map<String, Future<Map<K, V>>> storage;

    private ExecutorService outputThreadPool;

    public CacheOutput(ExecutorService outputThreadPool) {
        this.storage = new ConcurrentHashMap<>();
        this.outputThreadPool = outputThreadPool;
    }

    @Override
    public void process(CruncherDataFrame<K, V> cruncherDataFrame) {
        outputThreadPool.submit(new StoreOutputTask<>(this, cruncherDataFrame));
    }

    public void aggregate(String newName, List<String> existingResults, BiFunction<V, V, V> aggregatingFunction, Function<String, Void> itemProcessedCallback) {
        outputThreadPool.submit(new AggregatorTask<>(newName, existingResults, this, aggregatingFunction, itemProcessedCallback));
    }

    @Override
    public void store(String name, Future<Map<K, V>> data) {
        this.storage.put(name, data);
    }

    @Override
    public Map<K, V> take(String name) {
        try {
            return this.storage.get(name).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<K, V> poll(String name) {
        if (! this.storage.get(name).isDone())
        {
            return null;
        }
        return this.take(name);
    }

    public ExecutorService getOutputThreadPool() {
        return outputThreadPool;
    }
}
