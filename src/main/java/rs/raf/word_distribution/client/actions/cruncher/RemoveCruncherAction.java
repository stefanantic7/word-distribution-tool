package rs.raf.word_distribution.client.actions.cruncher;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import rs.raf.word_distribution.client.views.MainStage;
import rs.raf.word_distribution.counter_cruncher.CounterCruncher;
import rs.raf.word_distribution.client.views.cruncher.CruncherDetailsBox;

public class RemoveCruncherAction implements EventHandler<ActionEvent> {

    private CounterCruncher counterCruncher;
    private CruncherDetailsBox cruncherDetailsBox;

    public RemoveCruncherAction(CounterCruncher counterCruncher, CruncherDetailsBox cruncherDetailsBox) {
        this.counterCruncher = counterCruncher;
        this.cruncherDetailsBox = cruncherDetailsBox;
    }

    @Override
    public void handle(ActionEvent event) {
        // Remove from all inputs
        MainStage.getInstance().getFileInputsView().getFileInputsSet().forEach(fileInput -> {
            fileInput.unlinkCruncher(counterCruncher);
        });
        // TODO remove from list view

        counterCruncher.destroy();
        ((Pane) this.cruncherDetailsBox.getParent()).getChildren().remove(this.cruncherDetailsBox);
        this.cruncherDetailsBox.getCruncherObservableList().remove(counterCruncher);
    }
}
