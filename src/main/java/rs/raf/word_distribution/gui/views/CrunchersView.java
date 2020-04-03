package rs.raf.word_distribution.gui.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import rs.raf.word_distribution.AppCore;
import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;
import rs.raf.word_distribution.counter_cruncher.CounterCruncher;

import java.util.Optional;

public class CrunchersView extends VBox {

    private ObservableList<Cruncher<?, ?>> cruncherObservableList;
    public CrunchersView() {
        this.cruncherObservableList = FXCollections.observableArrayList();

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
                Cruncher<BagOfWords, Integer> cruncher = new CounterCruncher(arity, AppCore.getCruncherThreadPool());
                cruncherObservableList.add(cruncher);
                AppCore.getInputThreadPool().execute(cruncher);
            });
        });

        this.getChildren().addAll(cruncherLabel, addCruncherButton);
    }

    public ObservableList<Cruncher<?, ?>> getCruncherObservableList() {
        return cruncherObservableList;
    }
}
