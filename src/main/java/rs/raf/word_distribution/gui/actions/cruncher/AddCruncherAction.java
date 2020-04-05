package rs.raf.word_distribution.gui.actions.cruncher;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextInputDialog;
import rs.raf.word_distribution.AppCore;
import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.Output;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;
import rs.raf.word_distribution.counter_cruncher.CounterCruncher;
import rs.raf.word_distribution.gui.views.cruncher.CruncherDetailsBox;
import rs.raf.word_distribution.gui.views.cruncher.CrunchersView;

import java.util.Map;
import java.util.Optional;

public class AddCruncherAction implements EventHandler<ActionEvent> {

    private Output<BagOfWords, Integer> output;
    private CrunchersView crunchersView;

    public AddCruncherAction(Output<BagOfWords, Integer> output,
                             CrunchersView crunchersView) {
        this.output = output;
        this.crunchersView = crunchersView;
    }

    @Override
    public void handle(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Confirmation");
        dialog.setHeaderText("Enter cruncher arity");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(arityString -> {
            int arity = Integer.parseInt(result.get());

            CounterCruncher counterCruncher = new CounterCruncher(arity, AppCore.getCruncherThreadPool());
            counterCruncher.addOutput(this.output);
            AppCore.getInputThreadPool().execute(counterCruncher);

            this.crunchersView.getCruncherObservableList().add(counterCruncher);

            CruncherDetailsBox cruncherDetailsBox = new CruncherDetailsBox(counterCruncher, this.crunchersView.getCruncherObservableList());

            this.crunchersView.getCruncherCruncherDetailsBoxMap().put(counterCruncher, cruncherDetailsBox);

            this.crunchersView.getChildren().addAll(cruncherDetailsBox);
        });
    }
}
