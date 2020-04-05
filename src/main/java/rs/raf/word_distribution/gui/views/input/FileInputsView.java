package rs.raf.word_distribution.gui.views.input;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import rs.raf.word_distribution.Config;
import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.file_input.Disk;
import rs.raf.word_distribution.file_input.FileInput;
import rs.raf.word_distribution.gui.actions.input.*;

import java.io.File;

public class FileInputsView extends VBox {

    private ObservableList<Disk> allocatedDisksObservableList;

    private ObservableList<Cruncher<?, ?>> cruncherObservableList;

    public FileInputsView(ObservableList<Cruncher<?, ?>> cruncherObservableList) {
        this.cruncherObservableList = cruncherObservableList;
        this.allocatedDisksObservableList = FXCollections.observableArrayList();

        this.init();
    }

    private void init() {
        this.setSpacing(5);
        this.setPadding(new Insets(5));

        BooleanProperty addFileInputButtonEnabledProperty = new SimpleBooleanProperty(false);

        Label fileInputsLabel = new Label("File inputs:");

        ComboBox<Disk> diskComboBox = new ComboBox<>(FXCollections.observableArrayList(Config.DISKS));

        diskComboBox.setOnAction(
                new ChooseDiskAction(diskComboBox, addFileInputButtonEnabledProperty, allocatedDisksObservableList)
        );
        this.allocatedDisksObservableList.addListener((ListChangeListener<Disk>) c -> {
            c.next();
            addFileInputButtonEnabledProperty.set(!this.allocatedDisksObservableList.contains(diskComboBox.getValue()));
        });


        Button addFileInputButton = new Button("Add File input");
        addFileInputButton.disableProperty().bind(addFileInputButtonEnabledProperty.not());
        addFileInputButton.setOnAction(
                new AddInputAction(this,
                        diskComboBox.valueProperty(),
                        this.allocatedDisksObservableList,
                        this.cruncherObservableList)
        );


        this.getChildren().addAll(fileInputsLabel, diskComboBox, addFileInputButton);
    }
}
