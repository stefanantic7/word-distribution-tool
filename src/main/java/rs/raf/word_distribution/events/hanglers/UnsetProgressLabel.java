package rs.raf.word_distribution.events.hanglers;

import javafx.application.Platform;
import rs.raf.word_distribution.events.EventListener;
import rs.raf.word_distribution.events.EventType;
import rs.raf.word_distribution.file_input.FileInput;
import rs.raf.word_distribution.client.views.MainStage;

public class UnsetProgressLabel implements EventListener {
    @Override
    public void handleEvent(EventType eventType, Object... args) {
        Platform.runLater(() -> {
            FileInput fileInput = (FileInput) args[0];
            MainStage.getInstance().getFileInputsView().updateProcessStatus(fileInput, null);
        });
    }
}
