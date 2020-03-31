package rs.raf.word_distribution.cache_output;

import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class StoreOutputTask implements Runnable {

    private CacheOutput cacheOutput;

    private CruncherDataFrame cruncherDataFrame;

    public StoreOutputTask(CacheOutput cacheOutput, CruncherDataFrame cruncherDataFrame) {
        this.cacheOutput = cacheOutput;
        this.cruncherDataFrame = cruncherDataFrame;
    }

    @Override
    public void run() {
        System.out.println("Storing: " + cruncherDataFrame.getName());
        // TODO: Set * to name

        this.cacheOutput.store(cruncherDataFrame.getName(), cruncherDataFrame.getData());
        try {
            Map<BagOfWords, Integer> result = ((Future<Map<BagOfWords, Integer>>) cruncherDataFrame.getData()).get();

            System.out.println("Stored: " + result.keySet().size());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }
}
