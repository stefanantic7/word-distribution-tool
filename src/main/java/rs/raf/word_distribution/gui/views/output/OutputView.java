package rs.raf.word_distribution.gui.views.output;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import rs.raf.word_distribution.AppCore;
import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.Output;
import rs.raf.word_distribution.cache_output.CacheOutput;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;
import rs.raf.word_distribution.gui.actions.output.ShowSingleResultAction;
import rs.raf.word_distribution.gui.actions.output.SumResultsAction;
import rs.raf.word_distribution.gui.tasks.SortResultsTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class OutputView extends VBox {

    private Output<BagOfWords, Integer> output;

    private ListView<OutputListItem> entryListView;
    private Map<CruncherDataFrame<?, ?>, OutputListItem> cruncherDataFrameOutputListItemMap;

    public OutputView() {
        this.cruncherDataFrameOutputListItemMap = new ConcurrentHashMap<>();
        this.init();

        // TODO: move this
        this.output = new CacheOutput<>(AppCore.getOutputThreadPool());
        AppCore.getOutputThreadPool().submit(output);
    }

    public Output<BagOfWords, Integer> getOutput() {
        return output;
    }

    public ListView<OutputListItem> getEntryListView() {
        return entryListView;
    }

    private void init() {
        this.setSpacing(5);
        this.setPadding(new Insets(5));

        this.entryListView = new ListView<>();
        this.entryListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Button singleResultButton = new Button("Single result");
        singleResultButton.disableProperty().bind(entryListView.getSelectionModel().selectedItemProperty().isNull());

        singleResultButton.setOnAction(new ShowSingleResultAction(this));


        Button sumResultButton = new Button("Sum result");
        sumResultButton.disableProperty().bind(entryListView.getSelectionModel().selectedItemProperty().isNull());

        sumResultButton.setOnAction(new SumResultsAction(this));

        this.getChildren().addAll(entryListView, singleResultButton, sumResultButton);
    }

    public synchronized void addNewItem(CruncherDataFrame<?, ?> cruncherDataFrame) {
        OutputListItem newOutputListItem = new OutputListItem(cruncherDataFrame.getName());
        newOutputListItem.setTitle("*" + newOutputListItem.getOriginalName());

        OutputListItem outputListItem = this.cruncherDataFrameOutputListItemMap.putIfAbsent(cruncherDataFrame, newOutputListItem);

        if (outputListItem == null) {
            this.entryListView.getItems().add(newOutputListItem);
        }
    }

    public synchronized void changeItemToFinished(CruncherDataFrame<?, ?> cruncherDataFrame) {
        OutputListItem newOutputListItem = new OutputListItem(cruncherDataFrame.getName());
        this.cruncherDataFrameOutputListItemMap.putIfAbsent(cruncherDataFrame, newOutputListItem);

        OutputListItem outputListItem = this.cruncherDataFrameOutputListItemMap.get(cruncherDataFrame);

        outputListItem.setTitle(cruncherDataFrame.getName());
        this.entryListView.refresh();
    }
}
