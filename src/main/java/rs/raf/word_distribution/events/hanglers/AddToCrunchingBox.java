package rs.raf.word_distribution.events.hanglers;

import javafx.application.Platform;
import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.events.EventListener;
import rs.raf.word_distribution.events.EventType;
import rs.raf.word_distribution.client.views.MainStage;

public class AddToCrunchingBox implements EventListener {

    @Override
    public void handleEvent(EventType eventType, Object... args) {
        Platform.runLater(() -> {
            MainStage.getInstance().getCrunchersView().addCrunchingItem((Cruncher<?, ?>) args[0], (CruncherDataFrame<?, ?>) args[1]);
        });
    }

}
