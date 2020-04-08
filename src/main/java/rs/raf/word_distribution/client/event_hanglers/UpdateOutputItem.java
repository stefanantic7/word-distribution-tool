package rs.raf.word_distribution.client.event_hanglers;

import javafx.application.Platform;
import rs.raf.word_distribution.client.views.MainStage;
import rs.raf.word_distribution.observer.Listener;
import rs.raf.word_distribution.observer.events.OutputGainedAccessToCruncherDataFrame;

public class UpdateOutputItem extends Listener<OutputGainedAccessToCruncherDataFrame> {

    @Override
    protected void handleEvent(OutputGainedAccessToCruncherDataFrame event) {
        Platform.runLater(() -> {
            MainStage.getInstance().getOutputView().changeItemToFinished(event.getCruncherDataFrame());
        });
    }
}
