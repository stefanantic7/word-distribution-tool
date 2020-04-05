package rs.raf.word_distribution.gui.views.cruncher;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import rs.raf.word_distribution.AppCore;
import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.Output;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;
import rs.raf.word_distribution.counter_cruncher.CounterCruncher;
import rs.raf.word_distribution.gui.actions.cruncher.RemoveCruncherAction;

public class CruncherDetailsBox extends VBox {

    private CounterCruncher counterCruncher;
    private ObservableList<Cruncher<?, ?>> cruncherObservableList;
    private CrunchingListView crunchingListView;

    public CruncherDetailsBox(CounterCruncher counterCruncher,
                              ObservableList<Cruncher<?, ?>> cruncherObservableList) {
        this.counterCruncher = counterCruncher;
        this.cruncherObservableList = cruncherObservableList;

        this.crunchingListView = new CrunchingListView();

        this.init();
    }

    private void init() {
        Label cruncherNameLabel = new Label("Name: Cruncher " + this.counterCruncher.getArity());
        Button removeCruncherButton = new Button("Remove cruncher");

        removeCruncherButton.setOnAction(new RemoveCruncherAction(counterCruncher, this));

        this.getChildren().addAll(cruncherNameLabel, removeCruncherButton, crunchingListView);
    }

    public void addCrunchingItem(String cruncherName) {
        this.crunchingListView.add(cruncherName);
    }

    public void removeCrunchingItem(String cruncherName) {
        this.crunchingListView.remove(cruncherName);
    }

    public ObservableList<Cruncher<?, ?>> getCruncherObservableList() {
        return cruncherObservableList;
    }
}
