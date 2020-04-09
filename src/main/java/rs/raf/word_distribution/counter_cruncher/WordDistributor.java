package rs.raf.word_distribution.counter_cruncher;

import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.InputDataFrame;
import rs.raf.word_distribution.observer.EventManager;
import rs.raf.word_distribution.observer.events.CruncherFinishedEvent;
import rs.raf.word_distribution.observer.events.CruncherStartedEvent;
import rs.raf.word_distribution.observer.events.OutOfMemoryEvent;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;

public class WordDistributor implements Runnable {

    private CounterCruncher counterCruncher;
    private InputDataFrame inputDataFrame;

    public WordDistributor(InputDataFrame inputDataFrame, CounterCruncher counterCruncher) {
        this.inputDataFrame = inputDataFrame;
        this.counterCruncher = counterCruncher;
    }

    @Override
    public void run() {
        CruncherDataFrame<BagOfWords, Integer> cruncherDataFrame = this.generateCruncherDataFrame();
        if (cruncherDataFrame == null) {
            return;
        }
        this.counterCruncher.broadcastCruncherDataFrame(cruncherDataFrame);

        EventManager.getInstance().notify(new CruncherStartedEvent(counterCruncher, cruncherDataFrame));
        try {
            cruncherDataFrame.getFuture().get();
        } catch (OutOfMemoryError outOfMemoryError) {
            EventManager.getInstance().notify(new OutOfMemoryEvent(outOfMemoryError));
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        EventManager.getInstance().notify(new CruncherFinishedEvent(counterCruncher, cruncherDataFrame));

    }

    private CruncherDataFrame<BagOfWords, Integer> generateCruncherDataFrame() {
        String dataFrameName = this.generateDataFrameName();

        String content = this.inputDataFrame.getContent();

        WordCounterTask wordCounterTask
                = new WordCounterTask(0, content.length(), content, this.counterCruncher.getArity());

        try {
            Future<Map<BagOfWords, Integer>> futureResult
                    = this.counterCruncher.getCruncherThreadPool().submit(wordCounterTask);

            return new CruncherDataFrame<>(dataFrameName, futureResult);
        } catch (RejectedExecutionException rejectedExecutionException) {
            return null;
        }
    }

    private String generateDataFrameName() {
        return inputDataFrame.getSource() + "-arity"+this.counterCruncher.getArity();
    }
}
