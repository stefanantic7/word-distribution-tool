package rs.raf.word_distribution.cache_output;

import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.Output;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;
import rs.raf.word_distribution.events.EventManager;
import rs.raf.word_distribution.events.EventType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AggregatorTask<K, V> implements Runnable {

    private String newName;

    private List<String> existingResults;

    private CacheOutput<K, V> cacheOutput;

    private final BiFunction<V, V, V> aggregatingFunction;

    private Function<String, Void> itemProcessedCallback;

    public AggregatorTask(String newName,
                          List<String> existingResults,
                          CacheOutput<K, V> cacheOutput,
                          BiFunction<V, V, V> aggregatingFunction,
                          Function<String, Void> itemProcessedCallback) {
        this.newName = newName;
        this.existingResults = existingResults;
        this.cacheOutput = cacheOutput;
        this.aggregatingFunction = aggregatingFunction;
        this.itemProcessedCallback = itemProcessedCallback;
    }

    @Override
    public void run() {
        try {
            this.aggregate();
        } catch (OutOfMemoryError outOfMemoryError) {
            EventManager.getInstance().notify(EventType.OUT_OF_MEMORY);
        }
    }

    private void aggregate() {
        System.out.println("Aggregating " + this.newName);

        Future<Map<K, V>> futureResult = this.cacheOutput.getOutputThreadPool().submit(() -> {
            Map<K, V> bagOfWordsIntegerMap = new HashMap<>();

            for (String existingResult : existingResults) {
                Map<K, V> existingMap = this.cacheOutput.take(existingResult);

                // Merging
                for (Map.Entry<K, V> entry : existingMap.entrySet()) {
                    bagOfWordsIntegerMap.merge(entry.getKey(), entry.getValue(), this.aggregatingFunction);
                }

                itemProcessedCallback.apply(existingResult);
            }

            return bagOfWordsIntegerMap;
        });

        this.cacheOutput.store(newName, futureResult);
        CruncherDataFrame<K, V> cruncherDataFrame = new CruncherDataFrame<>(this.newName, futureResult);
        EventManager.getInstance().notify(EventType.STORED_CRUNCHER_DATA_FRAME, cruncherDataFrame);

        try {
            futureResult.get();
        } catch (OutOfMemoryError outOfMemoryError) {
            EventManager.getInstance().notify(EventType.OUT_OF_MEMORY);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        EventManager.getInstance().notify(EventType.AGGREGATION_FINISHED, cruncherDataFrame);
    }
}
