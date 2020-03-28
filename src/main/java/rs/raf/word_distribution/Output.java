package rs.raf.word_distribution;

import rs.raf.word_distribution.counter_cruncher.BagOfWords;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Output {
    protected BlockingQueue<Future<CruncherDataFrame<Map<BagOfWords,Integer>>>> cruncherDataFrameFutureBlockingQueue;

    public Output() {
        this.cruncherDataFrameFutureBlockingQueue = new LinkedBlockingQueue<>();
    }

    public void putCruncherDataFrameFuture(Future<CruncherDataFrame<Map<BagOfWords, Integer>>> cruncherDataFrameFuture) {
        this.cruncherDataFrameFutureBlockingQueue.add(cruncherDataFrameFuture);
    }
}
