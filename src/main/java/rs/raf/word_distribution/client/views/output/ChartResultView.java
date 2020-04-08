package rs.raf.word_distribution.client.views.output;

import javafx.scene.chart.*;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;

public class ChartResultView extends StackPane {

    private Map<Number, Number> results;

    private LineChart<Number, Number> lineChart;

    public ChartResultView() {
        this(new HashMap<>());
    }

    public ChartResultView(Map<Number, Number> results) {
        this.results = results;
        this.init();
    }

    private void init() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Word");

        this.lineChart = new LineChart<>(xAxis,yAxis);

        this.updateData("");

        this.getChildren().add(lineChart);
    }

    public void updateResult(Map<Number, Number> results, String resultsName) {
        this.results = results;
        this.lineChart.getData().clear();
        this.updateData(resultsName);
    }

    private void updateData(String dataName) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(dataName);

        this.results.forEach((k, v) -> {
            series.getData().add(new XYChart.Data<>(k, v));
        });

        lineChart.getData().add(series);
    }
}
