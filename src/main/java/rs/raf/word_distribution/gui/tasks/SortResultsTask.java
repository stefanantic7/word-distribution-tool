package rs.raf.word_distribution.gui.tasks;

import javafx.application.Platform;
import javafx.concurrent.Task;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;
import rs.raf.word_distribution.gui.views.MainStage;

import java.util.*;

public class SortResultsTask extends Task<Map<Number, Number>> {

    private Map<BagOfWords, Integer> results;

    public SortResultsTask(Map<BagOfWords, Integer> results) {
        this.results = results;
    }

    @Override
    protected void succeeded() {
        super.succeeded();

        Platform.runLater(() -> {
            MainStage.getInstance().getChartResultView().updateResult(this.getValue());
        });
    }

    @Override
    protected Map<Number, Number> call() throws Exception {
        List<Map.Entry<BagOfWords, Integer>> entryList = new ArrayList<>(this.results.entrySet());
        entryList.sort(new Comparator<Map.Entry<BagOfWords, Integer>>() {
            @Override
            public int compare(Map.Entry<BagOfWords, Integer> item1, Map.Entry<BagOfWords, Integer> item2) {
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
