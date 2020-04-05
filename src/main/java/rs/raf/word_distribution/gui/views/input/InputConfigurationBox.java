package rs.raf.word_distribution.gui.views.input;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.file_input.Disk;
import rs.raf.word_distribution.file_input.FileInput;
import rs.raf.word_distribution.gui.actions.input.*;

import java.io.File;

public class InputConfigurationBox extends VBox {

    private FileInput fileInput;
    private ObservableList<Cruncher<?, ?>> cruncherObservableList;
    private ObservableMap<FileInput, Disk> fileInputDiskObservableMap;
    private Label idleLabel;

    public InputConfigurationBox(FileInput fileInput,
                                 ObservableList<Cruncher<?, ?>> cruncherObservableList,
                                 ObservableMap<FileInput, Disk> fileInputDiskObservableMap) {
        this.fileInput = fileInput;
        this.cruncherObservableList = cruncherObservableList;
        this.fileInputDiskObservableMap = fileInputDiskObservableMap;
        this.init();
    }

    private void init() {
        this.setBorder(new Border(new BorderStroke(Color.LIGHTBLUE,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        this.setPadding(new Insets(10,  10,10,10));
        Label inputNameLabel = new Label("File input: " + this.fileInput.getDisk().getDiskPath());
        Label crunchersLabel = new Label("Crunchers");

        ListView<Cruncher<?, ?>> crunchersListView = new ListView<>();
        crunchersListView.setMaxHeight(200);

        ComboBox<Cruncher<?, ?>> crunchersComboBox = new ComboBox<>(this.cruncherObservableList);
        crunchersComboBox.setPromptText("Select cruncher");

        BooleanProperty linkCruncherButtonEnabledProperty = new SimpleBooleanProperty(false);
        Button linkCruncherButton = new Button("Link cruncher");
        crunchersComboBox.setOnAction(
                new ChooseCruncherAction(crunchersComboBox.valueProperty(), fileInput, linkCruncherButtonEnabledProperty)
        );

        linkCruncherButton.disableProperty().bind(linkCruncherButtonEnabledProperty.not());

        linkCruncherButton.setOnAction(
                new LinkCruncherAction(crunchersComboBox.valueProperty(),
                        linkCruncherButtonEnabledProperty,
                        fileInput,
                        crunchersListView.itemsProperty())
        );


        Button unlinkCruncherButton = new Button("Unlink cruncher");
        unlinkCruncherButton.disableProperty().bind(crunchersListView.getSelectionModel().selectedItemProperty().isNull());

        unlinkCruncherButton.setOnAction(
                new UnlinkCruncherAction(crunchersListView,
                        fileInput,
                        crunchersComboBox,
                        linkCruncherButtonEnabledProperty)
        );

        GridPane cruncherButtonsPane = new GridPane();
        cruncherButtonsPane.setPadding(new Insets(10, 0, 10, 0));
        cruncherButtonsPane.setVgap(10);
        cruncherButtonsPane.setHgap(10);
        cruncherButtonsPane.add(crunchersComboBox, 0, 0);
        cruncherButtonsPane.add(linkCruncherButton, 1, 0);
        cruncherButtonsPane.add(unlinkCruncherButton, 1, 1);


        ListView<File> dirListView = new ListView<>();
        dirListView.setMaxHeight(200);

        Button addDirButton = new Button("Add dir");

        addDirButton.setOnAction(new AddDirAction(fileInput, dirListView));

        Button removeDirButton = new Button("Remove dir");
        removeDirButton.disableProperty().bind(dirListView.getSelectionModel().selectedItemProperty().isNull());
        removeDirButton.setOnAction(new RemoveDirAction(dirListView, fileInput));


        Button removeInputButton = new Button("Remove input");

        removeInputButton.setOnAction(
                new RemoveInputAction(this, fileInput, fileInputDiskObservableMap)
        );


        Button pauseStartButton = new Button("Start");

        pauseStartButton.setOnAction(new PauseInputAction(fileInput, pauseStartButton));


        GridPane inputButtonsPane = new GridPane();
        inputButtonsPane.setPadding(new Insets(10, 0, 10, 0));
        inputButtonsPane.setVgap(10);
        inputButtonsPane.setHgap(10);
        inputButtonsPane.add(addDirButton, 0, 0);
        inputButtonsPane.add(removeDirButton, 1, 0);
        inputButtonsPane.add(pauseStartButton, 0, 1);
        inputButtonsPane.add(removeInputButton, 1, 1);

        this.idleLabel = new Label("Idle");

        this.getChildren().addAll(inputNameLabel, crunchersLabel, crunchersListView, cruncherButtonsPane,
                dirListView, inputButtonsPane, idleLabel);
    }

    public Label getIdleLabel() {
        return idleLabel;
    }
}
