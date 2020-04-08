package rs.raf.word_distribution.client.views.input;

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
import rs.raf.word_distribution.client.actions.input.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FileInputsView extends VBox {

    private ObservableMap<FileInput, Disk> fileInputDiskObservableMap;
    private Map<FileInput, StringProperty> fileInputCurrentProcessMap;
    private ObservableList<Cruncher<?, ?>> cruncherObservableList;
    private Map<FileInput, ObservableList<Cruncher<?, ?>>> fileInputCruncherObservableListMap;


    public FileInputsView(ObservableList<Cruncher<?, ?>> cruncherObservableList) {
        this.cruncherObservableList = cruncherObservableList;
        this.fileInputCurrentProcessMap = new HashMap<>();
        this.fileInputCruncherObservableListMap = new HashMap<>();
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
                        this.fileInputDiskObservableMap)
        );


        this.getChildren().addAll(fileInputsLabel, diskComboBox, addFileInputButton);
    }

    public Set<FileInput> getFileInputsSet() {
        return fileInputDiskObservableMap.keySet();
    }

    public ObservableList<Cruncher<?, ?>> getCruncherObservableList(FileInput fileInput) {
        return this.fileInputCruncherObservableListMap.get(fileInput);
    }

    public ObservableList<Cruncher<?, ?>> getCruncherObservableList() {
        return cruncherObservableList;
    }

    public void registerNewInput(FileInput fileInput, StringProperty currentProgressProperty, ObservableList<Cruncher<?, ?>> cruncherObservableList) {
        this.fileInputDiskObservableMap.put(fileInput, fileInput.getDisk());
        this.fileInputCurrentProcessMap.put(fileInput, currentProgressProperty);
        this.fileInputCruncherObservableListMap.put(fileInput, cruncherObservableList);
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
