package rs.raf.word_distribution.file_input;

import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.InputDataFrame;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

                Future<InputDataFrame> inputDataFrameFuture =
                        this.fileInput.getInputThreadPool().submit(new FileReader(file));

                this.passToCrunchers(inputDataFrameFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private void passToCrunchers(InputDataFrame inputDataFrame) {
        List<Cruncher<?, ?>> crunchers = this.fileInput.getCrunchers();

        for (Cruncher<?, ?> cruncher: crunchers) {
            cruncher.broadcastInputDataFrame(inputDataFrame);
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
