package rs.raf.word_distribution.observer.events;

import rs.raf.word_distribution.observer.Event;
import rs.raf.word_distribution.file_input.FileInput;

public class ReadingFinishedEvent extends Event {

    protected FileInput fileInput;

    public ReadingFinishedEvent(FileInput fileInput) {
        this.fileInput = fileInput;
    }

    public FileInput getFileInput() {
        return fileInput;
    }
}
