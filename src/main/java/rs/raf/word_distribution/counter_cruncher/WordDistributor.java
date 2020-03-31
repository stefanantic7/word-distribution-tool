package rs.raf.word_distribution.counter_cruncher;

import rs.raf.word_distribution.Config;
import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.InputDataFrame;
import rs.raf.word_distribution.Output;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class WordDistributor implements Runnable {

    private CounterCruncher counterCruncher;
    private InputDataFrame inputDataFrame;

    public WordDistributor(InputDataFrame inputDataFrame, CounterCruncher counterCruncher) {
        this.inputDataFrame = inputDataFrame;
        this.counterCruncher = counterCruncher;
    }

    @Override
    public void run() {
        String dataFrameName = inputDataFrame.getSource() + "-arity"+this.counterCruncher.getArity();

        String content = this.inputDataFrame.getContent();
        Future<Map<BagOfWords, Integer>> futureResult
                = this.counterCruncher.getCruncherThreadPool().submit(new WordCounterTask(0, content.length(), content, this.counterCruncher.getArity(), false));

        CruncherDataFrame cruncherDataFrame = new CruncherDataFrame(dataFrameName, futureResult);

        this.broadcastCruncherDataFrame(cruncherDataFrame);

        try {
            futureResult.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        /// TODO: remove gc
        System.gc();
    }

    private void broadcastCruncherDataFrame(CruncherDataFrame cruncherDataFrame) {
        for (Output output : this.counterCruncher.getOutputs()) {
            System.out.println("Broadcasting to outputs: "+cruncherDataFrame.getName());

            output.putCruncherDataFrame(cruncherDataFrame);
        }
    }
}
