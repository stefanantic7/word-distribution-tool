package rs.raf.word_distribution.client.actions;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import rs.raf.word_distribution.AppCore;
import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.Input;
import rs.raf.word_distribution.client.views.MainStage;

import java.util.concurrent.TimeUnit;

public class CloseApplicationAction implements EventHandler<WindowEvent> {
    @Override
    public void handle(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Okay, the program will be closed soon");
        alert.setContentText("Please wait...");
        alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        alert.initStyle(StageStyle.UNDECORATED);


        Thread t = new Thread(new CloseApplicationThread(alert));
        t.start();

        alert.showAndWait();
    }
}
