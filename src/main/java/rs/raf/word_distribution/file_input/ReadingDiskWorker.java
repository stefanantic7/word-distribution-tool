package rs.raf.word_distribution.file_input;

import rs.raf.word_distribution.InputDataFrame;
import rs.raf.word_distribution.observer.EventManager;
import rs.raf.word_distribution.observer.events.ReadingFinishedEvent;
import rs.raf.word_distribution.observer.events.ReadingStartedEvent;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

public class ReadingDiskWorker implements Runnable {

    private FileInput fileInput;

    public ReadingDiskWorker(FileInput fileInput) {
        this.fileInput = fileInput;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Optional<File> optionalFile = this.fileInput.getDisk().getReadingQueue().take();
                if (optionalFile.isEmpty()) {
                    break;
                }
                File file = optionalFile.get();

                Future<InputDataFrame> inputDataFrameFuture = this.fileInput.getInputTasksThreadPool().submit(new FileReader(file));
                /// READING
                EventManager.getInstance().notify(new ReadingStartedEvent(this.fileInput, file));

                this.fileInput.broadcastToCrunchers(inputDataFrameFuture.get());

                //// idle
                EventManager.getInstance().notify(new ReadingFinishedEvent(this.fileInput));
            } catch (RejectedExecutionException rejectedExecutionException) {
                return;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void destroy() {
        try {
            this.fileInput.getDisk().getReadingQueue().put(Optional.ofNullable(null));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
