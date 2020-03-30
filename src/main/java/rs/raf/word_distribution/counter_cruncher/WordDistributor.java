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
        Map<BagOfWords, Integer> bagsMap = new HashMap<>();
        String dataFrameName = inputDataFrame.getSource() + "-arity"+this.counterCruncher.getArity();
        CruncherDataFrame<Map<BagOfWords, Integer>> cruncherDataFrame
                = new CruncherDataFrame<>(dataFrameName, bagsMap);

        // Broadcast started event
        this.broadcastCruncherDataFrame(cruncherDataFrame);

        String content = this.inputDataFrame.getContent();
        Future<Map<BagOfWords, Integer>> result
                = this.counterCruncher.getCruncherThreadPool().submit(new WordCounterTask(0, content.length(), content, this.counterCruncher.getArity(), false));

        // Broadcast completed event
        try {
            cruncherDataFrame = cruncherDataFrame.complete(result.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        this.broadcastCruncherDataFrame(cruncherDataFrame);
    }

    private void broadcastCruncherDataFrame(CruncherDataFrame<Map<BagOfWords, Integer>> cruncherDataFrame) {
        List<Output> outputs = this.counterCruncher.getOutputs();
        for (Output output : outputs) {
            System.out.println("Broadcasting to outputs: "+cruncherDataFrame.getName());
            System.out.println("Size: "+cruncherDataFrame.getData().keySet().size());
            output.putCruncherDataFrame(cruncherDataFrame);
        }
    }
}
