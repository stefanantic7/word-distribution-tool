package rs.raf.word_distribution.counter_cruncher;

import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.InputDataFrame;
import rs.raf.word_distribution.Output;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class WordCruncher implements Callable<CruncherDataFrame<Map<BagOfWords, Integer>>> {

    private CounterCruncher counterCruncher;
    private InputDataFrame inputDataFrame;

    public WordCruncher(InputDataFrame inputDataFrame, CounterCruncher counterCruncher) {
        this.inputDataFrame = inputDataFrame;
        this.counterCruncher = counterCruncher;
    }

    @Override
    public CruncherDataFrame<Map<BagOfWords, Integer>> call() {
        int arity = this.counterCruncher.getArity();
        Map<BagOfWords, Integer> bagsMap = new HashMap<>();

        String dataFrameName = inputDataFrame.getSource() + "-arity"+this.counterCruncher.getArity();
        return new CruncherDataFrame<>(dataFrameName, bagsMap);
    }
}
