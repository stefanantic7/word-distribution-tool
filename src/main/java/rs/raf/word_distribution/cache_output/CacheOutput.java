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

public class CacheOutput extends Output {

    private Map<String, Future<Map<BagOfWords, Integer>>> storage;

    private ExecutorService outputThreadPool;

    public CacheOutput(ExecutorService outputThreadPool) {
        this.storage = new ConcurrentHashMap<>();
        this.outputThreadPool = outputThreadPool;
    }

    @Override
    public void process(CruncherDataFrame cruncherDataFrame) {
        outputThreadPool.submit(new StoreOutputTask(this, cruncherDataFrame));
    }

    public void aggregate(String newName, List<String> existingResults) {
        outputThreadPool.submit(new AggregatorTask(newName, existingResults, this));
    }

    @Override
    public void store(String name, Object data) {
        this.storage.put(name, (Future<Map<BagOfWords, Integer>>) data);
    }

    @Override
    public Object take(String name) {
        try {
            return this.storage.get(name).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object poll(String name) {
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
