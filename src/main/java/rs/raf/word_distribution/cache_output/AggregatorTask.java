package rs.raf.word_distribution.cache_output;

import rs.raf.word_distribution.Output;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.BiFunction;

public class AggregatorTask<K, V> implements Runnable {

    private String newName;

    private List<String> existingResults;

    private CacheOutput<K, V> cacheOutput;

    private final BiFunction<V, V, V> aggregatingFunction;
    public AggregatorTask(String newName,
                          List<String> existingResults,
                          CacheOutput<K, V> cacheOutput,
                          BiFunction<V, V, V> aggregatingFunction) {
        this.newName = newName;
        this.existingResults = existingResults;
        this.cacheOutput = cacheOutput;
        this.aggregatingFunction = aggregatingFunction;
    }

    @Override
    public void run() {
        System.out.println("Aggregating " + this.newName);

        // TODO: add *

        Future<Map<K, V>> futureResult = this.cacheOutput.getOutputThreadPool().submit(() -> {
            Map<K, V> bagOfWordsIntegerMap = new HashMap<>();

            for (String existingResult : existingResults) {
                Map<K, V> existingMap = this.cacheOutput.take(existingResult);

                // Merging
                for (Map.Entry<K, V> entry : existingMap.entrySet()) {
                    bagOfWordsIntegerMap.merge(entry.getKey(), entry.getValue(), this.aggregatingFunction);
                }
            }

            return bagOfWordsIntegerMap;
        });

        this.cacheOutput.store(newName, futureResult);

        try {
            futureResult.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // TODO: remove *

    }
}
