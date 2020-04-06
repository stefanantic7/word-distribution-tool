package rs.raf.word_distribution.cache_output;

import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.events.EventManager;
import rs.raf.word_distribution.events.EventType;

import java.util.concurrent.ExecutionException;

public class StoreOutputTask<K, V> implements Runnable {

    private CacheOutput<K, V> cacheOutput;

    private CruncherDataFrame<K, V> cruncherDataFrame;

    public StoreOutputTask(CacheOutput<K, V> cacheOutput, CruncherDataFrame<K, V> cruncherDataFrame) {
        this.cacheOutput = cacheOutput;
        this.cruncherDataFrame = cruncherDataFrame;
    }

    @Override
    public void run() {
        if(this.cacheOutput.poll(cruncherDataFrame.getName()) != null) {
            try {
                cruncherDataFrame.getFuture().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        this.cacheOutput.store(cruncherDataFrame.getName(), cruncherDataFrame.getFuture());
        EventManager.getInstance().notify(EventType.OUTPUT_STORED_CRUNCHER_DATA_FRAME, cruncherDataFrame);

        this.cacheOutput.take(cruncherDataFrame.getName());
        EventManager.getInstance().notify(EventType.OUTPUT_GAINED_ACCESS_TO_CRUNCHER_DATA_FRAME, cruncherDataFrame);
    }
}
