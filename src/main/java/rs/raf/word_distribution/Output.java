package rs.raf.word_distribution;

import rs.raf.word_distribution.counter_cruncher.BagOfWords;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Output implements Runnable {
    protected BlockingQueue<CruncherDataFrame> cruncherDataFrameBlockingQueue;

    public Output() {
        this.cruncherDataFrameBlockingQueue = new LinkedBlockingQueue<>();
    }

    public abstract void process(CruncherDataFrame cruncherDataFrame);

    public abstract void store(String name, Object data);

    public abstract Object take(String name);

    public abstract Object poll(String name);

    public void putCruncherDataFrame(CruncherDataFrame cruncherDataFrame) {
        this.cruncherDataFrameBlockingQueue.add(cruncherDataFrame);
    }

    @Override
    public void run() {
        while (true) {
            try {
                CruncherDataFrame cruncherDataFrame = this.cruncherDataFrameBlockingQueue.take();

                this.process(cruncherDataFrame);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
