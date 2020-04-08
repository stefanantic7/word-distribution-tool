package rs.raf.word_distribution.client.actions.input;

import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import rs.raf.word_distribution.file_input.Disk;
import rs.raf.word_distribution.file_input.FileInput;

public class ChooseDiskAction implements EventHandler<ActionEvent> {

    private ComboBox<Disk> diskComboBox;
    private BooleanProperty addFileInputButtonEnabledProperty;
    private ObservableMap<FileInput, Disk> fileInputDiskObservableMap;

    public ChooseDiskAction(ComboBox<Disk> diskComboBox,
                            BooleanProperty addFileInputButtonEnabledProperty,
                            ObservableMap<FileInput, Disk> fileInputDiskObservableMap) {
        this.diskComboBox = diskComboBox;
        this.addFileInputButtonEnabledProperty = addFileInputButtonEnabledProperty;
        this.fileInputDiskObservableMap = fileInputDiskObservableMap;
    }

    @Override
    public void handle(ActionEvent event) {
        if (diskComboBox.valueProperty().isNull().get()) {
            addFileInputButtonEnabledProperty.set(false);
        } else {
            addFileInputButtonEnabledProperty.set(!this.fileInputDiskObservableMap.containsValue(diskComboBox.getValue()));
        }
    }
}
