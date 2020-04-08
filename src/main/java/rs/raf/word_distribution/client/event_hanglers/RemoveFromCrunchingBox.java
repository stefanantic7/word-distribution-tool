package rs.raf.word_distribution.client.event_hanglers;

import javafx.application.Platform;
import rs.raf.word_distribution.client.views.MainStage;
import rs.raf.word_distribution.observer.Listener;
import rs.raf.word_distribution.observer.events.CruncherFinishedEvent;

public class RemoveFromCrunchingBox extends Listener<CruncherFinishedEvent> {

    @Override
    protected void handleEvent(CruncherFinishedEvent event) {
        Platform.runLater(() -> {
            MainStage.getInstance().getCrunchersView().removeCrunchingItem(event.getCruncher(), event.getCruncherDataFrame());
        });
    }
}
