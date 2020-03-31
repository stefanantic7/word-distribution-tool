package rs.raf.word_distribution.cache_output;

import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.Output;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class CacheOutput extends Output {

    private Map<String, Object> storage;

    private ExecutorService outputThreadPool;

    public CacheOutput(ExecutorService outputThreadPool) {
        this.storage = new ConcurrentHashMap<>();
        this.outputThreadPool = outputThreadPool;
    }

    @Override
    public void process(CruncherDataFrame cruncherDataFrame) {
        outputThreadPool.submit(new StoreOutputTask(this, cruncherDataFrame));
    }

    @Override
    public void store(String name, Object data) {
        this.storage.put(name, data);
    }

    @Override
    public Object get(String name) {
        return this.storage.get(name);
    }
}
