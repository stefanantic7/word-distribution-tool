package rs.raf.word_distribution.client.views.cruncher;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.counter_cruncher.CounterCruncher;
import rs.raf.word_distribution.client.actions.cruncher.RemoveCruncherAction;

public class CruncherDetailsBox extends VBox {

    private CounterCruncher counterCruncher;
    private CrunchingListView crunchingListView;

    public CruncherDetailsBox(CounterCruncher counterCruncher) {
        this.counterCruncher = counterCruncher;

        this.crunchingListView = new CrunchingListView();

        this.init();
    }

    private void init() {
        this.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        this.setPadding(new Insets(5,  5,0,5));

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

}
