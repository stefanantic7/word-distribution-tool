package rs.raf.word_distribution.client.views.output;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import rs.raf.word_distribution.AppCore;
import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.Output;
import rs.raf.word_distribution.cache_output.CacheOutput;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;
import rs.raf.word_distribution.client.actions.output.ShowSingleResultAction;
import rs.raf.word_distribution.client.actions.output.SumResultsAction;

public class OutputView extends VBox {

    private Output<BagOfWords, Integer> output;

    private ListView<OutputListItem> entryListView;

    public OutputView() {
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
        sumResultButton.setDisable(true);
        entryListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<OutputListItem>) c -> {
            c.next();
            sumResultButton.setDisable(entryListView.getSelectionModel().getSelectedItems().size() <= 1);
        });

        sumResultButton.setOnAction(new SumResultsAction(this));

        this.getChildren().addAll(entryListView, singleResultButton, sumResultButton);
    }

    public synchronized void addNewItem(CruncherDataFrame<?, ?> cruncherDataFrame) {
        OutputListItem newOutputListItem = new OutputListItem(cruncherDataFrame.getName());
        if(!this.entryListView.getItems().contains(newOutputListItem)) {
            newOutputListItem.setTitle("*" + cruncherDataFrame.getName());
            this.entryListView.getItems().add(newOutputListItem);
        }
    }

    public synchronized void changeItemToFinished(CruncherDataFrame<?, ?> cruncherDataFrame) {
        OutputListItem newOutputListItem = new OutputListItem(cruncherDataFrame.getName());
        if(this.entryListView.getItems().contains(newOutputListItem)) {
            this.entryListView.getItems().set(this.entryListView.getItems().indexOf(newOutputListItem), newOutputListItem);
        } else {
            this.entryListView.getItems().add(newOutputListItem);
        }
    }
}
