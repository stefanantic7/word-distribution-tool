package rs.raf.word_distribution.gui.views;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;

import java.util.Map;

public class OutputView extends VBox {

    public OutputView() {
        this.setSpacing(5);
        this.setPadding(new Insets(5));
        this.init();
    }

    private void init() {
        ListView<Map.Entry<BagOfWords, Integer>> entryListView = new ListView<>();
        Button singleResultButton = new Button("Single result");
        Button sumResultButton = new Button("Single result");

        this.getChildren().addAll(entryListView, singleResultButton, sumResultButton);
    }
}
