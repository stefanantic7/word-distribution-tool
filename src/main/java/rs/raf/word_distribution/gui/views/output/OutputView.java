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
        singleResultButton.disableProperty().bind(entryListView.getSelectionModel().selectedItemProperty().isNull());

        singleResultButton.setOnAction(e -> {
            Map<BagOfWords, Integer> results = this.output.poll(entryListView.getSelectionModel().getSelectedItem().getOriginalName());
            if (results == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Selected item is still in progress");
                alert.setContentText("Please wait and try again later.");
                alert.showAndWait();
                return;
            }

            SortResultsTask sortResultsTask = new SortResultsTask(results);

            ProgressBar progressBar = new ProgressBar();
            progressBar.progressProperty().bind(sortResultsTask.progressProperty());
            sortResultsTask.setOnSucceeded(event -> {
                this.getChildren().remove(progressBar);
            });

            Thread ttt = new Thread(sortResultsTask);
            ttt.start();

            this.getChildren().add(progressBar);
        });


        Button sumResultButton = new Button("Sum result");
        sumResultButton.disableProperty().bind(entryListView.getSelectionModel().selectedItemProperty().isNull());

        sumResultButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Confirmation");
            dialog.setHeaderText("Enter sum name");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(sumName -> {
                ProgressBar progressBar = new ProgressBar();
                progressBar.setProgress(0);

                List<String> selected = entryListView.getSelectionModel().getSelectedItems().stream()
                        .map(OutputListItem::getOriginalName).collect(Collectors.toList());
                AtomicInteger finishedItems = new AtomicInteger(0);
                this.output.aggregate(sumName, selected, Integer::sum, s -> {
                    int finished = finishedItems.incrementAndGet();
                    Platform.runLater(() -> {
                        progressBar.setProgress(finished / (float)selected.size());

                        if (finished / (float) selected.size() == 1) {
                            getChildren().remove(progressBar);
                        }
                    });
                    return null;
                });

                this.getChildren().add(progressBar);
            });
        });

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
