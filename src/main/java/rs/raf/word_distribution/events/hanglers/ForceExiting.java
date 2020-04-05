package rs.raf.word_distribution.events.hanglers;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;
import rs.raf.word_distribution.AppCore;
import rs.raf.word_distribution.events.EventListener;
import rs.raf.word_distribution.events.EventType;

public class ForceExiting implements EventListener {
    @Override
    public void handleEvent(EventType eventType, Object... args) {

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Out of memory while reading");
            alert.setContentText("Aborting");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.showAndWait();

            AppCore.getInputThreadPool().shutdown();
            AppCore.getOutputThreadPool().shutdown();

            Platform.exit();
            System.exit(1);
        });
    }
}
