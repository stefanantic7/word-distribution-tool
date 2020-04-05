package rs.raf.word_distribution.gui.actions.input;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import rs.raf.word_distribution.file_input.FileInput;

import java.io.File;

public class RemoveDirAction implements EventHandler<ActionEvent> {

    private ListView<File> dirListView;
    private FileInput fileInput;

    public RemoveDirAction(ListView<File> dirListView, FileInput fileInput) {
        this.dirListView = dirListView;
        this.fileInput = fileInput;
    }

    @Override
    public void handle(ActionEvent event) {
        File file = this.dirListView.getSelectionModel().getSelectedItem();
        this.fileInput.removeDir(file);
        this.dirListView.getItems().remove(file);
    }
}
