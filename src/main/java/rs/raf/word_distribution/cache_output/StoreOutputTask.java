package rs.raf.word_distribution.cache_output;

import rs.raf.word_distribution.CruncherDataFrame;

import java.util.Map;

public class StoreOutputTask implements Runnable {

    private CacheOutput cacheOutput;

    private CruncherDataFrame cruncherDataFrame;

    public StoreOutputTask(CacheOutput cacheOutput, CruncherDataFrame cruncherDataFrame) {
        this.cacheOutput = cacheOutput;
        this.cruncherDataFrame = cruncherDataFrame;
    }

    @Override
    public void run() {
        if (cruncherDataFrame.isCompleted()) {
            System.out.println("Storing: " + cruncherDataFrame.getName() +", size: " + ((Map)cruncherDataFrame.getData()).keySet().size());
        }

        this.cacheOutput.store(cruncherDataFrame.getName(), cruncherDataFrame.getData());
    }
}
