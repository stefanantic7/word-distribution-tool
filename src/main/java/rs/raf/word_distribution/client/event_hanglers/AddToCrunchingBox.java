package rs.raf.word_distribution.client.event_hanglers;

import javafx.application.Platform;
import rs.raf.word_distribution.client.views.MainStage;
import rs.raf.word_distribution.observer.Listener;
import rs.raf.word_distribution.observer.events.CruncherStartedEvent;

public class AddToCrunchingBox extends Listener<CruncherStartedEvent> {

    @Override
    public void handleEvent(CruncherStartedEvent event) {
        Platform.runLater(() -> {
            MainStage.getInstance().getCrunchersView().addCrunchingItem(event.getCruncher(), event.getCruncherDataFrame());
        });
    }

}
