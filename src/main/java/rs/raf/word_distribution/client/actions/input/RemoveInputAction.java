package rs.raf.word_distribution.client.actions.input;

import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import rs.raf.word_distribution.file_input.Disk;
import rs.raf.word_distribution.file_input.FileInput;
import rs.raf.word_distribution.client.views.input.InputConfigurationBox;

public class RemoveInputAction implements EventHandler<ActionEvent> {

    private InputConfigurationBox inputConfigurationBox;
    private FileInput fileInput;
    private ObservableMap<FileInput, Disk> fileInputDiskObservableMap;

    public RemoveInputAction(InputConfigurationBox inputConfigurationBox,
                             FileInput fileInput,
                             ObservableMap<FileInput, Disk> fileInputDiskObservableMap) {
        this.inputConfigurationBox = inputConfigurationBox;
        this.fileInput = fileInput;
        this.fileInputDiskObservableMap = fileInputDiskObservableMap;
    }

    @Override
    public void handle(ActionEvent event) {
        ((Pane) this.inputConfigurationBox.getParent()).getChildren().remove(this.inputConfigurationBox);

        this.fileInputDiskObservableMap.remove(fileInput);
        this.fileInput.destroy();
    }
}
