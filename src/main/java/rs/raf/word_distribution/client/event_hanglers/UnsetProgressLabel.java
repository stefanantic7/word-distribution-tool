package rs.raf.word_distribution.client.event_hanglers;

import javafx.application.Platform;
import rs.raf.word_distribution.observer.Listener;
import rs.raf.word_distribution.observer.events.ReadingFinishedEvent;
import rs.raf.word_distribution.client.views.MainStage;

public class UnsetProgressLabel extends Listener<ReadingFinishedEvent> {

    @Override
    protected void handleEvent(ReadingFinishedEvent event) {
        Platform.runLater(() -> {
            MainStage.getInstance().getFileInputsView().updateProcessStatus(event.getFileInput(), null);
        });
    }
}
