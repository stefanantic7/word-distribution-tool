package rs.raf.word_distribution.counter_cruncher;

import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.InputDataFrame;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RejectedExecutionException;

public class CounterCruncher extends Cruncher<BagOfWords, Integer> {

    protected int arity;

    private ForkJoinPool cruncherTasksThreadPool;

    public CounterCruncher(int arity, ForkJoinPool cruncherTasksThreadPool) {
        super();
        this.arity = arity;
        this.cruncherTasksThreadPool = cruncherTasksThreadPool;
    }

    @Override
    public void handle(InputDataFrame inputDataFrame) {
        System.out.println("handling: "+inputDataFrame.getSource());
        try {
            cruncherTasksThreadPool.submit(new WordDistributor(inputDataFrame, this));
        } catch (RejectedExecutionException ignored) {
        }
    }

    public int getArity() {
        return arity;
    }

    public ForkJoinPool getCruncherTasksThreadPool() {
        return cruncherTasksThreadPool;
    }

    @Override
    public String toString() {
        return "CounterCruncher, arity=" + this.getArity();
    }
}
