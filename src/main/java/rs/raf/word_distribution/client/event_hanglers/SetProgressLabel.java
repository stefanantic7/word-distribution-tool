package rs.raf.word_distribution.client.event_hanglers;

import javafx.application.Platform;
import rs.raf.word_distribution.observer.Listener;
import rs.raf.word_distribution.observer.events.ReadingStartedEvent;
import rs.raf.word_distribution.client.views.MainStage;

public class SetProgressLabel extends Listener<ReadingStartedEvent> {

    @Override
    protected void handleEvent(ReadingStartedEvent event) {
        Platform.runLater(() -> {
            MainStage.getInstance().getFileInputsView().updateProcessStatus(event.getFileInput(), event.getFile().getName());
        });
    }
}
