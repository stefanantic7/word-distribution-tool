package rs.raf.word_distribution.gui.views.input;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rs.raf.word_distribution.Config;
import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.Input;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;
import rs.raf.word_distribution.file_input.Disk;
import rs.raf.word_distribution.file_input.FileInput;
import rs.raf.word_distribution.gui.actions.AddInputAction;

import java.io.File;
import java.util.stream.Stream;

public class FileInputsView extends VBox {

    private ObservableList<Disk> allocatedDisksObservableList;

    private ObservableList<Cruncher<?, ?>> cruncherObservableList;

    public FileInputsView(ObservableList<Cruncher<?, ?>> cruncherObservableList) {
        this.cruncherObservableList = cruncherObservableList;
        this.allocatedDisksObservableList = FXCollections.observableArrayList();
        this.setSpacing(5);
        this.setPadding(new Insets(5));

        this.init();
    }

    private void init() {
        BooleanProperty addButtonEnabledProperty = new SimpleBooleanProperty(false);

        Label fileInputsLabel = new Label("File inputs:");

        ComboBox<Disk> diskComboBox = new ComboBox<>(FXCollections.observableArrayList(Config.DISKS));
        diskComboBox.setOnAction(e -> {
            if (diskComboBox.valueProperty().isNull().get()) {
                addButtonEnabledProperty.set(false);
            } else {
                addButtonEnabledProperty.set(!this.allocatedDisksObservableList.contains(diskComboBox.getValue()));
            }
        });
        this.allocatedDisksObservableList.addListener((ListChangeListener<Disk>) c -> {
            c.next();
            addButtonEnabledProperty.set(!this.allocatedDisksObservableList.contains(diskComboBox.getValue()));
        });


        Button addFileInputButton = new Button("Add File input");
        addFileInputButton.disableProperty().bind(addButtonEnabledProperty.not());
        addFileInputButton.setOnAction(new AddInputAction(this, diskComboBox.valueProperty()));


        this.getChildren().addAll(fileInputsLabel, diskComboBox, addFileInputButton);
    }

    public void addInputConfiguration(FileInput fileInput) {
        VBox inputConfigurationBox = new VBox();
        Label inputNameLabel = new Label("File input: " + fileInput.getDisk().getDiskPath());
        Label crunchersLabel = new Label("Crunchers");

        ListView<Cruncher<?, ?>> crunchersListView = new ListView<>();
        crunchersListView.setMaxHeight(200);

        ComboBox<Cruncher<?, ?>> crunchersComboBox = new ComboBox<>(this.cruncherObservableList);
        crunchersComboBox.setPromptText("Select cruncher");

        BooleanProperty linkCruncherButtonEnabledProperty = new SimpleBooleanProperty(false);
        Button linkCruncherButton = new Button("Link cruncher");
        crunchersComboBox.setOnAction(e -> {
            Cruncher<?, ?> cruncher = crunchersComboBox.getValue();
            if (cruncher == null) {
                linkCruncherButtonEnabledProperty.set(false);
            } else {
                linkCruncherButtonEnabledProperty.set(!fileInput.getCrunchers().contains(cruncher));
            }
        });

        linkCruncherButton.disableProperty().bind(linkCruncherButtonEnabledProperty.not());
        linkCruncherButton.setOnAction(e -> {
            Cruncher<?, ?> cruncher = crunchersComboBox.getValue();

            linkCruncherButtonEnabledProperty.set(false);
            fileInput.linkCruncher(cruncher);
            crunchersListView.getItems().add(cruncher);
        });


        Button unlinkCruncherButton = new Button("Unlink cruncher");
        unlinkCruncherButton.disableProperty().bind(crunchersListView.getSelectionModel().selectedItemProperty().isNull());
        unlinkCruncherButton.setOnAction(e -> {
            Cruncher<?, ?> cruncher = crunchersListView.getSelectionModel().getSelectedItem();
            fileInput.getCrunchers().remove(cruncher);
            crunchersListView.getItems().remove(cruncher);

            if(crunchersComboBox.getValue() == cruncher) {
                linkCruncherButtonEnabledProperty.set(true);
            }
        });

        GridPane cruncherButtonsPane = new GridPane();
        cruncherButtonsPane.setVgap(5);
        cruncherButtonsPane.setHgap(10);
        cruncherButtonsPane.add(crunchersComboBox, 0, 0);
        cruncherButtonsPane.add(linkCruncherButton, 1, 0);
        cruncherButtonsPane.add(unlinkCruncherButton, 1, 1);


        ListView<File> dirListView = new ListView<>();
        dirListView.setMaxHeight(200);

        Button addDirButton = new Button("Add dir");
        addDirButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File(fileInput.getDisk().getDiskPath()));
            File selectedFile = directoryChooser.showDialog(null);

            fileInput.addDir(selectedFile);
            dirListView.getItems().add(selectedFile);
        });

        Button removeDirButton = new Button("Remove dir");
        removeDirButton.disableProperty().bind(dirListView.getSelectionModel().selectedItemProperty().isNull());
        removeDirButton.setOnAction(e -> {
            File file = dirListView.getSelectionModel().getSelectedItem();
            fileInput.removeDir(file);
            dirListView.getItems().remove(file);
        });


        Button removeInputButton = new Button("Remove input");
        removeInputButton.setOnAction(e -> {
            this.getChildren().remove(inputConfigurationBox);
            fileInput.destroy();
            this.allocatedDisksObservableList.remove(fileInput.getDisk());
        });


        Button pauseInputButton = new Button("Start");
        pauseInputButton.setOnAction(e -> {
            if (fileInput.isRunning()) {
                fileInput.pause();
                pauseInputButton.setText("Start");
            } else {
                fileInput.start();
                pauseInputButton.setText("Pause");
            }
        });





        GridPane inputButtonsPane = new GridPane();
        inputButtonsPane.setVgap(5);
        inputButtonsPane.setHgap(10);
        inputButtonsPane.add(addDirButton, 0, 0);
        inputButtonsPane.add(removeDirButton, 1, 0);
        inputButtonsPane.add(pauseInputButton, 0, 1);
        inputButtonsPane.add(removeInputButton, 1, 1);

        allocatedDisksObservableList.add(fileInput.getDisk());
        inputConfigurationBox.getChildren().addAll(inputNameLabel, crunchersLabel, crunchersListView, cruncherButtonsPane, dirListView, inputButtonsPane);
        this.getChildren().addAll(inputConfigurationBox);
    }
}
