package rs.raf.word_distribution.events.hanglers;

import javafx.application.Platform;
import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.events.EventListener;
import rs.raf.word_distribution.events.EventType;
import rs.raf.word_distribution.client.views.MainStage;

public class AddToOutputView implements EventListener {
    @Override
    public void handleEvent(EventType eventType, Object... args) {
        Platform.runLater(() -> {
            MainStage.getInstance().getOutputView().addNewItem((CruncherDataFrame<?, ?>)args[0]);
        });
    }
}
