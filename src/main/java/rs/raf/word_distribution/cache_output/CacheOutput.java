package rs.raf.word_distribution.cache_output;

import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.Output;
import rs.raf.word_distribution.observer.EventManager;
import rs.raf.word_distribution.observer.events.OutOfMemoryEvent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CacheOutput<K, V> extends Output<K, V> {

    private Map<String, Future<Map<K, V>>> storage;

    private ExecutorService outputTasksThreadPool;

    public CacheOutput(ExecutorService outputTasksThreadPool) {
        this.storage = new ConcurrentHashMap<>();
        this.outputTasksThreadPool = outputTasksThreadPool;
    }

    @Override
    public void process(CruncherDataFrame<K, V> cruncherDataFrame) {
        try {
            outputTasksThreadPool.submit(new StoreOutputTask<>(this, cruncherDataFrame));
        } catch (RejectedExecutionException ignored) {
        }
    }

    @Override
    public void aggregate(String newName, List<String> existingResults, BiFunction<V, V, V> aggregatingFunction, Function<String, Void> itemProcessedCallback) {
        try {
            outputTasksThreadPool.submit(new AggregatorTask<>(newName, existingResults, this, aggregatingFunction, itemProcessedCallback));
        } catch (RejectedExecutionException ignored) {
        }
    }

    @Override
    public void store(String name, Future<Map<K, V>> data) {
        this.storage.put(name, data);
    }

    @Override
    public Map<K, V> take(String name) {
        try {
            return this.storage.get(name).get();
        } catch (OutOfMemoryError outOfMemoryError) {
            EventManager.getInstance().notify(new OutOfMemoryEvent(outOfMemoryError));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<K, V> poll(String name) {
        if (this.storage.get(name) == null || !this.storage.get(name).isDone())
        {
            return null;
        }
        return this.take(name);
    }

    public ExecutorService getOutputTasksThreadPool() {
        return outputTasksThreadPool;
    }
}
