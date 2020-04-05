package rs.raf.word_distribution.gui.actions.cruncher;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import rs.raf.word_distribution.counter_cruncher.CounterCruncher;
import rs.raf.word_distribution.gui.views.cruncher.CruncherDetailsBox;

public class RemoveCruncherAction implements EventHandler<ActionEvent> {

    private CounterCruncher counterCruncher;
    private CruncherDetailsBox cruncherDetailsBox;

    public RemoveCruncherAction(CounterCruncher counterCruncher, CruncherDetailsBox cruncherDetailsBox) {
        this.counterCruncher = counterCruncher;
        this.cruncherDetailsBox = cruncherDetailsBox;
    }

    @Override
    public void handle(ActionEvent event) {
        counterCruncher.destroy();
        ((Pane) this.cruncherDetailsBox.getParent()).getChildren().remove(this.cruncherDetailsBox);
        this.cruncherDetailsBox.getCruncherObservableList().remove(counterCruncher);
    }
}
