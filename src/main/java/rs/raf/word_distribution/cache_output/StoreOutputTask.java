package rs.raf.word_distribution.cache_output;

import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class StoreOutputTask<K, V> implements Runnable {

    private CacheOutput<K, V> cacheOutput;

    private CruncherDataFrame<K, V> cruncherDataFrame;

    public StoreOutputTask(CacheOutput<K, V> cacheOutput, CruncherDataFrame<K, V> cruncherDataFrame) {
        this.cacheOutput = cacheOutput;
        this.cruncherDataFrame = cruncherDataFrame;
    }

    @Override
    public void run() {
        System.out.println("Storing: " + cruncherDataFrame.getName());
        // TODO: Add * to name

        this.cacheOutput.store(cruncherDataFrame.getName(), cruncherDataFrame.getFuture());
        try {
            Map<K, V> result = cruncherDataFrame.getFuture().get();

            System.out.println("Stored: " + result.keySet().size());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }
}
