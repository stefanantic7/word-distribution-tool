package rs.raf.word_distribution.counter_cruncher;

import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.InputDataFrame;
import rs.raf.word_distribution.Output;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class CounterCruncher extends Cruncher<BagOfWords, Integer> {

    protected int arity;

    private ForkJoinPool cruncherThreadPool;

    public CounterCruncher(int arity, ForkJoinPool cruncherThreadPool) {
        super();
        this.arity = arity;
        this.cruncherThreadPool = cruncherThreadPool;
    }

    @Override
    public void handle(InputDataFrame inputDataFrame) {
        System.out.println("handling: "+inputDataFrame.getSource());
        cruncherThreadPool.submit(new WordDistributor(inputDataFrame, this));
    }

    public int getArity() {
        return arity;
    }

    public ForkJoinPool getCruncherThreadPool() {
        return cruncherThreadPool;
    }

    @Override
    public String toString() {
        return "CounterCruncher, arity=" + this.getArity();
    }
}
