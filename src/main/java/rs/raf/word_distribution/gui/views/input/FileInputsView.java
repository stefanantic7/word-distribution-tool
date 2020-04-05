package rs.raf.word_distribution.gui.views.input;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import rs.raf.word_distribution.Config;
import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.file_input.Disk;
import rs.raf.word_distribution.file_input.FileInput;
import rs.raf.word_distribution.gui.actions.input.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FileInputsView extends VBox {

    private ObservableMap<FileInput, Disk> fileInputDiskObservableMap;
    private Map<FileInput, StringProperty> fileInputCurrentProcessMap;
    private ObservableList<Cruncher<?, ?>> cruncherObservableList;


    public FileInputsView(ObservableList<Cruncher<?, ?>> cruncherObservableList) {
        this.cruncherObservableList = cruncherObservableList;
        this.fileInputCurrentProcessMap = new HashMap<>();
        this.fileInputDiskObservableMap = FXCollections.observableHashMap();

        this.init();
    }

    private void init() {
        this.setSpacing(5);
        this.setPadding(new Insets(5));

        BooleanProperty addFileInputButtonEnabledProperty = new SimpleBooleanProperty(false);

        Label fileInputsLabel = new Label("File inputs:");

        ComboBox<Disk> diskComboBox = new ComboBox<>(FXCollections.observableArrayList(Config.DISKS));

        diskComboBox.setOnAction(
                new ChooseDiskAction(diskComboBox, addFileInputButtonEnabledProperty, fileInputDiskObservableMap)
        );
        this.fileInputDiskObservableMap.addListener((MapChangeListener<FileInput, Disk>) change -> {
            addFileInputButtonEnabledProperty.set(!this.fileInputDiskObservableMap.containsValue(diskComboBox.getValue()));
        });


        Button addFileInputButton = new Button("Add File input");
        addFileInputButton.disableProperty().bind(addFileInputButtonEnabledProperty.not());
        addFileInputButton.setOnAction(
                new AddInputAction(this,
                        diskComboBox.valueProperty(),
                        this.fileInputDiskObservableMap,
                        this.cruncherObservableList)
        );


        this.getChildren().addAll(fileInputsLabel, diskComboBox, addFileInputButton);
    }

    public Set<FileInput> getFileInputsSet() {
        return fileInputDiskObservableMap.keySet();
    }

    public void registerNewInput(FileInput fileInput, StringProperty currentProgressProperty) {
        this.fileInputDiskObservableMap.put(fileInput, fileInput.getDisk());
        this.fileInputCurrentProcessMap.put(fileInput, currentProgressProperty);
    }

    public void updateProcessStatus(FileInput fileInput, String newProcessStatus) {
        StringProperty progressStatus = this.fileInputCurrentProcessMap.get(fileInput);
        if (newProcessStatus == null) {
            progressStatus.set("idle");
        } else {
            progressStatus.set(newProcessStatus);
        }

    }
}
