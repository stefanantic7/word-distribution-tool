package rs.raf.word_distribution.client.event_hanglers;

import javafx.application.Platform;
import rs.raf.word_distribution.client.views.MainStage;
import rs.raf.word_distribution.observer.Listener;
import rs.raf.word_distribution.observer.events.OutputStoredCruncherDataFrameEvent;

public class AddToOutputView extends Listener<OutputStoredCruncherDataFrameEvent> {

    @Override
    public void handleEvent(OutputStoredCruncherDataFrameEvent event) {
        Platform.runLater(() -> {
            MainStage.getInstance().getOutputView().addNewItem(event.getCruncherDataFrame());
        });
    }
}
