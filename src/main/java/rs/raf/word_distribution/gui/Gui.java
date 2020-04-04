package rs.raf.word_distribution.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import rs.raf.word_distribution.gui.views.MainStage;

public class Gui extends Application {


    public static void show() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        MainStage.getInstance();
    }
}
