package rs.raf.word_distribution;

import rs.raf.word_distribution.counter_cruncher.BagOfWords;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Output {
    protected BlockingQueue<CruncherDataFrame<Map<BagOfWords,Integer>>> cruncherDataFrameBlockingQueue;

    public Output() {
        this.cruncherDataFrameBlockingQueue = new LinkedBlockingQueue<>();
    }

    public void putCruncherDataFrame(CruncherDataFrame<Map<BagOfWords, Integer>> cruncherDataFrame) {
        this.cruncherDataFrameBlockingQueue.add(cruncherDataFrame);
    }
}
