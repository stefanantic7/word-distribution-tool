package rs.raf.word_distribution.gui.actions.input;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import rs.raf.word_distribution.file_input.Disk;
import rs.raf.word_distribution.file_input.FileInput;
import rs.raf.word_distribution.gui.views.input.FileInputsView;
import rs.raf.word_distribution.gui.views.input.InputConfigurationBox;

public class RemoveInputAction implements EventHandler<ActionEvent> {

    private InputConfigurationBox inputConfigurationBox;
    private FileInput fileInput;
    private ObservableList<Disk> allocatedDisksObservableList;

    public RemoveInputAction(InputConfigurationBox inputConfigurationBox, FileInput fileInput, ObservableList<Disk> allocatedDisksObservableList) {
        this.inputConfigurationBox = inputConfigurationBox;
        this.fileInput = fileInput;
        this.allocatedDisksObservableList = allocatedDisksObservableList;
    }

    @Override
    public void handle(ActionEvent event) {
        ((Pane) this.inputConfigurationBox.getParent()).getChildren().remove(this.inputConfigurationBox);

        this.allocatedDisksObservableList.remove(fileInput.getDisk());
        this.fileInput.destroy();
    }
}
