package rs.raf.word_distribution.observer.events;

import rs.raf.word_distribution.observer.Event;
import rs.raf.word_distribution.file_input.FileInput;

import java.io.File;

public class ReadingStartedEvent extends Event {

    protected FileInput fileInput;
    protected File file;

    public ReadingStartedEvent(FileInput fileInput, File file) {
        this.fileInput = fileInput;
        this.file = file;
    }

    public FileInput getFileInput() {
        return fileInput;
    }

    public File getFile() {
        return file;
    }
}
