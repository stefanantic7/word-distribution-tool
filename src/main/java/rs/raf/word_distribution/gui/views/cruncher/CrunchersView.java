package rs.raf.word_distribution.gui.views.cruncher;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import rs.raf.word_distribution.*;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;
import rs.raf.word_distribution.counter_cruncher.CounterCruncher;
import rs.raf.word_distribution.gui.actions.cruncher.AddCruncherAction;

import java.util.*;

public class CrunchersView extends VBox {

    private ObservableList<Cruncher<?, ?>> cruncherObservableList;

    private Output<BagOfWords, Integer> output;

    private Map<Cruncher<?, ?>, CruncherDetailsBox> cruncherCruncherDetailsBoxMap;

    public CrunchersView(Output<BagOfWords, Integer> output) {
        this.output = output;
        this.cruncherObservableList = FXCollections.observableArrayList();
        this.cruncherCruncherDetailsBoxMap = new HashMap<>();

        init();
    }

    private void init() {
        this.setSpacing(5);
        this.setPadding(new Insets(5));

        Label cruncherLabel = new Label("Crunchers");
        Button addCruncherButton = new Button("Add Cruncher");
        addCruncherButton.setOnAction(new AddCruncherAction(this.output, this));

        this.getChildren().addAll(cruncherLabel, addCruncherButton);
    }

    public void addCrunchingItem(Cruncher<?, ?> cruncher, CruncherDataFrame<?, ?> cruncherDataFrame) {
        this.cruncherCruncherDetailsBoxMap.get(cruncher).addCrunchingItem(cruncherDataFrame.getName());
    }

    public void removeCrunchingItem(Cruncher<?, ?> cruncher, CruncherDataFrame<?, ?> cruncherDataFrame) {
        this.cruncherCruncherDetailsBoxMap.get(cruncher).removeCrunchingItem(cruncherDataFrame.getName());
    }

    public ObservableList<Cruncher<?, ?>> getCruncherObservableList() {
        return cruncherObservableList;
    }

    public Map<Cruncher<?, ?>, CruncherDetailsBox> getCruncherCruncherDetailsBoxMap() {
        return cruncherCruncherDetailsBoxMap;
    }
}
