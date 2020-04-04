package rs.raf.word_distribution.gui.views.cruncher;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import rs.raf.word_distribution.*;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;
import rs.raf.word_distribution.counter_cruncher.CounterCruncher;
import rs.raf.word_distribution.gui.views.cruncher.CrunchingBox;

import java.util.*;

public class CrunchersView extends VBox {

    private ObservableList<Cruncher<?, ?>> cruncherObservableList;

    private Output<BagOfWords, Integer> output;

    private Map<Cruncher<?, ?>, CrunchingBox> cruncherCrunchingBoxMap;

    public CrunchersView(Output<BagOfWords, Integer> output) {
        this.output = output;
        this.cruncherObservableList = FXCollections.observableArrayList();
        this.cruncherCrunchingBoxMap = new HashMap<>();

        this.setSpacing(5);
        this.setPadding(new Insets(5));
        init();
    }

    private void init() {
        Label cruncherLabel = new Label("Crunchers");
        Button addCruncherButton = new Button("Add Cruncher");
        addCruncherButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Confirmation");
            dialog.setHeaderText("Enter cruncher arity");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(arityString -> {
                int arity = Integer.parseInt(result.get());

                Label cruncherNameLabel = new Label("Name: Cruncher " + arityString);
                Button removeCruncherButton = new Button("Remove cruncher");


                Cruncher<BagOfWords, Integer> cruncher = new CounterCruncher(arity, AppCore.getCruncherThreadPool());


                CrunchingBox crunchingBox = new CrunchingBox();
                this.cruncherCrunchingBoxMap.put(cruncher, crunchingBox);

                VBox cruncherDetailsBox = new VBox(cruncherNameLabel, removeCruncherButton, crunchingBox);


                removeCruncherButton.setOnAction(e1 -> {
                    this.getChildren().remove(cruncherDetailsBox);
                    cruncherObservableList.remove(cruncher);
                });

                this.getChildren().addAll(cruncherDetailsBox);
                cruncherObservableList.add(cruncher);
                cruncher.addOutput(this.output);
                AppCore.getInputThreadPool().execute(cruncher);
            });
        });

        this.getChildren().addAll(cruncherLabel, addCruncherButton);
    }

    public void addCrunchingItem(Cruncher<?, ?> cruncher, CruncherDataFrame<?, ?> cruncherDataFrame) {
        this.cruncherCrunchingBoxMap.get(cruncher).add(cruncherDataFrame.getName());
    }

    public void removeCrunchingItem(Cruncher<?, ?> cruncher, CruncherDataFrame<?, ?> cruncherDataFrame) {
        this.cruncherCrunchingBoxMap.get(cruncher).remove(cruncherDataFrame.getName());
    }

    public ObservableList<Cruncher<?, ?>> getCruncherObservableList() {
        return cruncherObservableList;
    }
}
