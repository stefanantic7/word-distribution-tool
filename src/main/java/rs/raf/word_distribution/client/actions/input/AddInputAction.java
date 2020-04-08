package rs.raf.word_distribution.client.actions.input;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import rs.raf.word_distribution.AppCore;
import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.file_input.Disk;
import rs.raf.word_distribution.file_input.FileInput;
import rs.raf.word_distribution.client.views.input.FileInputsView;
import rs.raf.word_distribution.client.views.input.InputConfigurationBox;

public class AddInputAction implements EventHandler<ActionEvent> {

    private FileInputsView fileInputsView;
    private ObjectProperty<Disk> diskObjectProperty;
    private ObservableMap<FileInput, Disk> fileInputDiskObservableMap;

    public AddInputAction(FileInputsView fileInputsView,
                          ObjectProperty<Disk> diskObjectProperty,
                          ObservableMap<FileInput, Disk> fileInputDiskObservableMap) {
        this.fileInputsView = fileInputsView;
        this.diskObjectProperty = diskObjectProperty;
        this.fileInputDiskObservableMap = fileInputDiskObservableMap;
    }

    @Override
    public void handle(ActionEvent event) {
        FileInput fileInput = new FileInput(diskObjectProperty.get(), AppCore.getInputThreadPool());
        fileInput.pause();
        AppCore.getInputThreadPool().submit(fileInput);

        InputConfigurationBox inputConfigurationBox
                = new InputConfigurationBox(fileInput, this.fileInputDiskObservableMap);

        this.fileInputsView.registerNewInput(fileInput,
                inputConfigurationBox.getIdleLabel().textProperty(),
                inputConfigurationBox.getCrunchersListView().getItems());

        this.fileInputsView.getChildren().addAll(inputConfigurationBox);
    }
}
