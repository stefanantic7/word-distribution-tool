package rs.raf.word_distribution.cache_output;

import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.observer.EventManager;
import rs.raf.word_distribution.observer.events.OutputGainedAccessToCruncherDataFrame;
import rs.raf.word_distribution.observer.events.OutputStoredCruncherDataFrameEvent;

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
        EventManager.getInstance().notify(new OutputStoredCruncherDataFrameEvent(cruncherDataFrame));

        this.cacheOutput.take(cruncherDataFrame.getName());
        EventManager.getInstance().notify(new OutputGainedAccessToCruncherDataFrame(cruncherDataFrame));
    }
}
