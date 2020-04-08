package rs.raf.word_distribution.client.tasks;

import javafx.application.Platform;
import javafx.concurrent.Task;
import rs.raf.word_distribution.Config;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;
import rs.raf.word_distribution.client.views.MainStage;

import java.util.*;

public class SortResultsTask extends Task<Map<Number, Number>> {

    private Map<BagOfWords, Integer> results;
    private String resultsName;

    public SortResultsTask(Map<BagOfWords, Integer> results, String resultsName) {
        this.results = results;
        this.resultsName = resultsName;
    }

    @Override
    protected void succeeded() {
        super.succeeded();

        Platform.runLater(() -> {
            MainStage.getInstance().getChartResultView().updateResult(this.getValue(), this.resultsName);
        });
    }

    long n = 0;

    @Override
    protected Map<Number, Number> call() throws Exception {
        List<Map.Entry<BagOfWords, Integer>> entryList = new ArrayList<>(this.results.entrySet());
        entryList.sort(new Comparator<Map.Entry<BagOfWords, Integer>>() {
            @Override
            public int compare(Map.Entry<BagOfWords, Integer> item1, Map.Entry<BagOfWords, Integer> item2) {
                n++;
                if(n % Config.SORT_PROGRESS_LIMIT == 0) {
                    updateProgress(n, results.size()*Math.log(results.size()));
                }
                return -Integer.compare(item1.getValue(), item2.getValue());
            }
        });

        int i = 0;
        Map<Number, Number> sortedResult = new LinkedHashMap<>();
        for (Map.Entry<BagOfWords, Integer> entry : entryList) {
            sortedResult.put(i, entry.getValue());
            i++;
            if (i == 100) {
                break;
            }
        }

        return sortedResult;
    }
}
