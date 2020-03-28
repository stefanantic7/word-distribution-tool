package rs.raf.word_distribution.file_input;

import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.InputDataFrame;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ReadingFileWorker implements Runnable {

    private FileInput fileInput;

    public ReadingFileWorker(FileInput fileInput) {
        this.fileInput = fileInput;
    }

    @Override
    public void run() {
        while (true) {
            if(this.fileInput.isRunning()) {
                try {
                    File file = this.fileInput.getDisk().getReadingQueue().take();

                    Future<InputDataFrame> inputDataFrameFuture =
                            this.fileInput.getInputThreadPool().submit(new FileReader(file));

                    this.passToCruncher(inputDataFrameFuture.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void passToCruncher(InputDataFrame inputDataFrame) {
        List<Cruncher> crunchers = this.fileInput.getCrunchers();

        for (Cruncher cruncher: crunchers) {
            cruncher.putInputDataFrame(inputDataFrame);
        }
    }
}
