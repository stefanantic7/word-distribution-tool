package rs.raf.word_distribution.client.event_hanglers;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import rs.raf.word_distribution.AppCore;
import rs.raf.word_distribution.observer.Listener;
import rs.raf.word_distribution.observer.events.OutOfMemoryEvent;

public class ForceExiting extends Listener<OutOfMemoryEvent> {

    @Override
    protected void handleEvent(OutOfMemoryEvent event) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Out of memory while reading");
            alert.setContentText("Aborting");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.showAndWait();

            AppCore.getInputThreadPool().shutdownNow();
            AppCore.getCruncherThreadPool().shutdownNow();
            AppCore.getOutputThreadPool().shutdownNow();

            AppCore.getInputTasksThreadPool().shutdownNow();
            AppCore.getOutputTasksThreadPool().shutdownNow();

            Platform.exit();
            System.exit(1);
        });
    }
}
