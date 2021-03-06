package rs.raf.word_distribution.client.actions.output;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import rs.raf.word_distribution.AppCore;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;
import rs.raf.word_distribution.client.tasks.SortResultsTask;
import rs.raf.word_distribution.client.views.output.OutputView;

import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

public class ShowSingleResultAction implements EventHandler<ActionEvent> {

    private OutputView outputView;

    public ShowSingleResultAction(OutputView outputView) {
        this.outputView = outputView;
    }

    @Override
    public void handle(ActionEvent event) {
        String itemName = this.outputView.getEntryListView().getSelectionModel().getSelectedItem().getOriginalName();
        Map<BagOfWords, Integer> results = this.outputView.getOutput().poll(itemName);
        if (results == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Selected item is still in progress");
            alert.setContentText("Please wait and try again later.");
            alert.showAndWait();
            return;
        }

        SortResultsTask sortResultsTask = new SortResultsTask(results, itemName);

        ProgressBar progressBar = new ProgressBar();
        progressBar.progressProperty().bind(sortResultsTask.progressProperty());
        sortResultsTask.setOnSucceeded(e -> {
            this.outputView.getChildren().remove(progressBar);
        });

        try {
            AppCore.getOutputTasksThreadPool().submit(sortResultsTask);
        } catch (RejectedExecutionException rejectedExecutionException) {
            return;
        }

        this.outputView.getChildren().add(progressBar);
    }
}
