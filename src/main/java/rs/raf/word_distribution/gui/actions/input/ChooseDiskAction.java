package rs.raf.word_distribution.gui.actions.input;

import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import rs.raf.word_distribution.file_input.Disk;

public class ChooseDiskAction implements EventHandler<ActionEvent> {

    private ComboBox<Disk> diskComboBox;
    private BooleanProperty addFileInputButtonEnabledProperty;
    private ObservableList<Disk> allocatedDisksObservableList;

    public ChooseDiskAction(ComboBox<Disk> diskComboBox,
                            BooleanProperty addFileInputButtonEnabledProperty,
                            ObservableList<Disk> allocatedDisksObservableList) {
        this.diskComboBox = diskComboBox;
        this.addFileInputButtonEnabledProperty = addFileInputButtonEnabledProperty;
        this.allocatedDisksObservableList = allocatedDisksObservableList;
    }

    @Override
    public void handle(ActionEvent event) {
        if (diskComboBox.valueProperty().isNull().get()) {
            addFileInputButtonEnabledProperty.set(false);
        } else {
            addFileInputButtonEnabledProperty.set(!this.allocatedDisksObservableList.contains(diskComboBox.getValue()));
        }
    }
}
