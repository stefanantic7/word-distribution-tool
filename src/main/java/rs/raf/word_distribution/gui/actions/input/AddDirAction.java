package rs.raf.word_distribution.gui.actions.input;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import rs.raf.word_distribution.Input;
import rs.raf.word_distribution.file_input.FileInput;

import java.io.File;

public class AddDirAction implements EventHandler<ActionEvent> {

    private FileInput fileInput;
    private ListView<File> dirListView;

    public AddDirAction(FileInput fileInput, ListView<File> dirListView) {
        this.fileInput = fileInput;
        this.dirListView = dirListView;
    }

    @Override
    public void handle(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(this.fileInput.getDisk().getDiskPath()));
        File selectedFile = directoryChooser.showDialog(null);

        if (selectedFile == null) {
            return;
        }

        this.fileInput.addDir(selectedFile);
        this.dirListView.getItems().add(selectedFile);
    }
}
