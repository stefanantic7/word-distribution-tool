package rs.raf.word_distribution.gui.actions.output;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextInputDialog;
import rs.raf.word_distribution.gui.views.output.OutputListItem;
import rs.raf.word_distribution.gui.views.output.OutputView;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SumResultsAction implements EventHandler<ActionEvent> {

    private OutputView outputView;

    public SumResultsAction(OutputView outputView) {
        this.outputView = outputView;
    }

    @Override
    public void handle(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Confirmation");
        dialog.setHeaderText("Enter sum name");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(sumName -> {
            ProgressBar progressBar = new ProgressBar();
            progressBar.setProgress(0);

            List<String> selected = this.outputView.getEntryListView().getSelectionModel().getSelectedItems().stream()
                    .map(OutputListItem::getOriginalName).collect(Collectors.toList());
            AtomicInteger finishedItems = new AtomicInteger(0);

            this.outputView.getOutput().aggregate(sumName, selected, Integer::sum, s -> {
                int finished = finishedItems.incrementAndGet();
                Platform.runLater(() -> {
                    progressBar.setProgress(finished / (float)selected.size());

                    if (finished / (float) selected.size() == 1) {
                        this.outputView.getChildren().remove(progressBar);
                    }
                });
                return null;
            });

            this.outputView.getChildren().add(progressBar);
        });
    }
}
