package rs.raf.word_distribution.gui.actions.input;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import rs.raf.word_distribution.AppCore;
import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.file_input.Disk;
import rs.raf.word_distribution.file_input.FileInput;
import rs.raf.word_distribution.gui.views.input.FileInputsView;
import rs.raf.word_distribution.gui.views.input.InputConfigurationBox;

public class AddInputAction implements EventHandler<ActionEvent> {

    private FileInputsView fileInputsView;
    private ObjectProperty<Disk> diskObjectProperty;
    private ObservableMap<FileInput, Disk> fileInputDiskObservableMap;
    private ObservableList<Cruncher<?, ?>> cruncherObservableList;

    public AddInputAction(FileInputsView fileInputsView,
                          ObjectProperty<Disk> diskObjectProperty,
                          ObservableMap<FileInput, Disk> fileInputDiskObservableMap,
                          ObservableList<Cruncher<?, ?>> cruncherObservableList) {
        this.fileInputsView = fileInputsView;
        this.diskObjectProperty = diskObjectProperty;
        this.fileInputDiskObservableMap = fileInputDiskObservableMap;
        this.cruncherObservableList = cruncherObservableList;
    }

    @Override
    public void handle(ActionEvent event) {
        FileInput fileInput = new FileInput(diskObjectProperty.get(), AppCore.getInputThreadPool());
        fileInput.pause();
        AppCore.getInputThreadPool().submit(fileInput);

        InputConfigurationBox inputConfigurationBox
                = new InputConfigurationBox(fileInput, this.cruncherObservableList, this.fileInputDiskObservableMap);

        this.fileInputsView.registerNewInput(fileInput, inputConfigurationBox.getIdleLabel().textProperty());

        this.fileInputsView.getChildren().addAll(inputConfigurationBox);
    }
}
