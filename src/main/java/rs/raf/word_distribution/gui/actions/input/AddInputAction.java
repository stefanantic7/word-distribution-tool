package rs.raf.word_distribution.gui.actions.input;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import rs.raf.word_distribution.AppCore;
import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.file_input.Disk;
import rs.raf.word_distribution.file_input.FileInput;
import rs.raf.word_distribution.file_input.ReadingDiskWorker;
import rs.raf.word_distribution.gui.views.input.FileInputsView;
import rs.raf.word_distribution.gui.views.input.InputConfigurationBox;

public class AddInputAction implements EventHandler<ActionEvent> {

    private FileInputsView fileInputsView;
    private ObjectProperty<Disk> diskObjectProperty;
    private ObservableList<Disk> allocatedDisksObservableList;
    private ObservableList<Cruncher<?, ?>> cruncherObservableList;

    public AddInputAction(FileInputsView fileInputsView,
                          ObjectProperty<Disk> diskObjectProperty,
                          ObservableList<Disk> allocatedDisksObservableList,
                          ObservableList<Cruncher<?, ?>> cruncherObservableList) {
        this.fileInputsView = fileInputsView;
        this.diskObjectProperty = diskObjectProperty;
        this.allocatedDisksObservableList = allocatedDisksObservableList;
        this.cruncherObservableList = cruncherObservableList;
    }

    @Override
    public void handle(ActionEvent event) {
        FileInput fileInput = new FileInput(diskObjectProperty.get(), AppCore.getInputThreadPool());
        fileInput.pause();
        AppCore.getInputThreadPool().submit(fileInput);

        this.allocatedDisksObservableList.add(fileInput.getDisk());

        InputConfigurationBox inputConfigurationBox
                = new InputConfigurationBox(fileInput, this.cruncherObservableList, this.allocatedDisksObservableList);

        this.fileInputsView.getChildren().addAll(inputConfigurationBox);
    }
}
