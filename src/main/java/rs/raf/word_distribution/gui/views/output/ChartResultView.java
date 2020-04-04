package rs.raf.word_distribution.gui.views.output;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.List;
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

        this. lineChart = new LineChart<>(xAxis,yAxis);

        this.updateData();

        this.getChildren().add(lineChart);
    }

    public void updateResult(Map<Number, Number> results) {
        this.results = results;
        this.lineChart.getData().clear();
        this.updateData();
    }

    private void updateData() {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        this.results.forEach((k, v) -> {
            series.getData().add(new XYChart.Data<>(k, v));
        });

        lineChart.getData().add(series);
    }
}
