package rs.raf.word_distribution.cache_output;

import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AggregatorTask implements Runnable {

    private String newName;

    private List<String> existingResults;

    private CacheOutput cacheOutput;

    public AggregatorTask(String newName, List<String> existingResults, CacheOutput cacheOutput) {
        this.newName = newName;
        this.existingResults = existingResults;
        this.cacheOutput = cacheOutput;
    }

    @Override
    public void run() {
        System.out.println("Aggregating " + this.newName);

        // TODO: add *

        Future<Map<BagOfWords, Integer>> futureResult = this.cacheOutput.getOutputThreadPool().submit(() -> {
            Map<BagOfWords, Integer> bagOfWordsIntegerMap = new HashMap<>();

            for (String existingResult : existingResults) {
                Map<BagOfWords, Integer> existingMap = (Map<BagOfWords, Integer>)this.cacheOutput.take(existingResult);

                // Merging
                for (Map.Entry<BagOfWords, Integer> entry : existingMap.entrySet()) {
                    bagOfWordsIntegerMap.merge(entry.getKey(), entry.getValue(), Integer::sum);
                }
            }

            return bagOfWordsIntegerMap;
        });
        this.cacheOutput.store(newName, futureResult);
        try {
            this.cacheOutput.store(newName, futureResult.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // TODO: remove *

    }
}
