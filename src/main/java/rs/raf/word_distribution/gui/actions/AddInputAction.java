package rs.raf.word_distribution.gui.actions;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import rs.raf.word_distribution.AppCore;
import rs.raf.word_distribution.Input;
import rs.raf.word_distribution.Utils;
import rs.raf.word_distribution.file_input.Disk;
import rs.raf.word_distribution.file_input.FileInput;
import rs.raf.word_distribution.gui.views.FileInputsView;

public class AddInputAction implements EventHandler<ActionEvent> {

    private FileInputsView fileInputsView;

    private ObjectProperty<Disk> diskObjectProperty;

    public AddInputAction(FileInputsView fileInputsView, ObjectProperty<Disk> diskObjectProperty) {
        this.fileInputsView = fileInputsView;
        this.diskObjectProperty = diskObjectProperty;
    }

    @Override
    public void handle(ActionEvent event) {
        FileInput input = new FileInput(diskObjectProperty.get(), AppCore.getInputThreadPool());
        input.pause();
        AppCore.getInputThreadPool().submit(input);

        // TODO: add this in InputView.
        this.fileInputsView.addInputConfiguration(input);
    }
}
