package rs.raf.word_distribution.gui.actions.input;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import rs.raf.word_distribution.Input;

public class PauseInputAction implements EventHandler<ActionEvent> {

    private Input input;
    private Button pauseStartButton;

    public PauseInputAction(Input input, Button pauseStartButton) {
        this.input = input;
        this.pauseStartButton = pauseStartButton;
    }

    @Override
    public void handle(ActionEvent event) {
        if (this.input.isRunning()) {
            this.input.pause();
            this.pauseStartButton.setText("Start");
        } else {
            this.input.start();
            this.pauseStartButton.setText("Pause");
        }
    }
}
