package rs.raf.word_distribution.events.hanglers;

import javafx.application.Platform;
import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.events.EventListener;
import rs.raf.word_distribution.events.EventType;
import rs.raf.word_distribution.client.views.MainStage;

public class UpdateOutputItem implements EventListener {
    @Override
    public void handleEvent(EventType eventType, Object... args) {

        CruncherDataFrame<?, ?> cruncherDataFrame = null;
        for (Object arg : args) {
            if (arg instanceof CruncherDataFrame) {
                cruncherDataFrame = (CruncherDataFrame<?, ?>) arg;
            }
        }
        if (cruncherDataFrame == null) {
            return;
        }

        CruncherDataFrame<?, ?> finalCruncherDataFrame = cruncherDataFrame;
        Platform.runLater(() -> {
            MainStage.getInstance().getOutputView().changeItemToFinished(finalCruncherDataFrame);
        });
    }
}
