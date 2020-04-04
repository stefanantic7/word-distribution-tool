package rs.raf.word_distribution.gui.views.output;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import rs.raf.word_distribution.AppCore;
import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.Output;
import rs.raf.word_distribution.cache_output.CacheOutput;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;
import rs.raf.word_distribution.gui.tasks.SortResultsTask;
import rs.raf.word_distribution.gui.views.MainStage;

import java.util.*;
import java.util.stream.Collectors;

public class OutputView extends VBox {

    private Output<BagOfWords, Integer> output;

    private ListView<OutputListItem> entryListView;
    private Map<CruncherDataFrame<?, ?>, OutputListItem> cruncherDataFrameOutputListItemMap;

    public OutputView() {
        this.cruncherDataFrameOutputListItemMap = new HashMap<>();
        this.setSpacing(5);
        this.setPadding(new Insets(5));
        this.init();

        this.output = new CacheOutput<>(AppCore.getOutputThreadPool());
        AppCore.getOutputThreadPool().submit(output);
    }

    public Output<BagOfWords, Integer> getOutput() {
        return output;
    }

    private void init() {
        this.entryListView = new ListView<>();
        this.entryListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Button singleResultButton = new Button("Single result");

        singleResultButton.setOnAction(e -> {
            Map<BagOfWords, Integer> results = this.output.poll(entryListView.getSelectionModel().getSelectedItem().getOriginalName());
            if (results == null) {
                return;
            }

            Thread ttt = new Thread(new SortResultsTask(results));
            ttt.start();

        });


        Button sumResultButton = new Button("Sum result");

        sumResultButton.setOnAction(e -> {
            List<String> selected = entryListView.getSelectionModel().getSelectedItems().stream().map(OutputListItem::getOriginalName).collect(Collectors.toList());
            this.output.aggregate("newName", selected, Integer::sum);
        });

        this.getChildren().addAll(entryListView, singleResultButton, sumResultButton);
    }

    public void addNewItem(CruncherDataFrame<?, ?> cruncherDataFrame) {
        OutputListItem outputListItem = new OutputListItem(cruncherDataFrame.getName());
        outputListItem.setTitle(outputListItem.getOriginalName() + "*");
        this.cruncherDataFrameOutputListItemMap.put(cruncherDataFrame, outputListItem);

        this.entryListView.getItems().add(outputListItem);
    }

    public void changeItemToFinished(CruncherDataFrame<?, ?> cruncherDataFrame) {
        OutputListItem outputListItem = this.cruncherDataFrameOutputListItemMap.get(cruncherDataFrame);
        outputListItem.setTitle(cruncherDataFrame.getName());
        this.entryListView.refresh();
    }
}
