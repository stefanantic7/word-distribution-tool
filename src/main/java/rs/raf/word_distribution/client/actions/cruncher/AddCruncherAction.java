package rs.raf.word_distribution.client.actions.cruncher;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import rs.raf.word_distribution.AppCore;
import rs.raf.word_distribution.Output;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;
import rs.raf.word_distribution.counter_cruncher.CounterCruncher;
import rs.raf.word_distribution.client.views.cruncher.CruncherDetailsBox;
import rs.raf.word_distribution.client.views.cruncher.CrunchersView;

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
            int arity = 0;
            try {
                arity = Integer.parseInt(result.get());
            } catch (NumberFormatException numberFormatException) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Your input should be a number.");
                alert.setContentText("Please create another one.");
                alert.showAndWait();
                return;
            }

            CounterCruncher counterCruncher = new CounterCruncher(arity, AppCore.getCruncherThreadPool());
            counterCruncher.addOutput(this.output);
            new Thread(counterCruncher).start();

            this.crunchersView.getCruncherObservableList().add(counterCruncher);

            CruncherDetailsBox cruncherDetailsBox = new CruncherDetailsBox(counterCruncher);

            this.crunchersView.getCruncherCruncherDetailsBoxMap().put(counterCruncher, cruncherDetailsBox);

            this.crunchersView.getChildren().addAll(cruncherDetailsBox);
        });
    }
}
