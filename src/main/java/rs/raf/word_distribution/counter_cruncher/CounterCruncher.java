package rs.raf.word_distribution.counter_cruncher;

import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.InputDataFrame;
import rs.raf.word_distribution.Output;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class CounterCruncher extends Cruncher {

    private ExecutorService cruncherThreadPool;

    public CounterCruncher(int arity, ExecutorService cruncherThreadPool) {
        super(arity);
        this.cruncherThreadPool = cruncherThreadPool;
    }

    @Override
    public void handle(InputDataFrame inputDataFrame) {
        Future<CruncherDataFrame<Map<BagOfWords, Integer>>> cruncherDataFrameFuture
                = cruncherThreadPool.submit(new WordCruncher(inputDataFrame, this));

        for (Output output: this.outputs) {
            output.putCruncherDataFrameFuture(cruncherDataFrameFuture);
        }
    }
}
