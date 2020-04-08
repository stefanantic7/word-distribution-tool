package rs.raf.word_distribution;

import rs.raf.word_distribution.events.EventManager;
import rs.raf.word_distribution.events.EventType;
import rs.raf.word_distribution.events.hanglers.*;
import rs.raf.word_distribution.client.Gui;

import java.util.concurrent.*;

public class AppCore {

    private static ExecutorService inputThreadPool = Executors.newCachedThreadPool();
    private static ForkJoinPool cruncherThreadPool = ForkJoinPool.commonPool();
    private static ExecutorService outputThreadPool = Executors.newCachedThreadPool();

    public static ExecutorService getInputThreadPool() {
        return inputThreadPool;
    }

    public static ForkJoinPool getCruncherThreadPool() {
        return cruncherThreadPool;
    }

    public static ExecutorService getOutputThreadPool() {
        return outputThreadPool;
    }

    public static void main(String[] args) {
        EventManager.getInstance().subscribe(EventType.READING_STARTED, new SetProgressLabel());
        EventManager.getInstance().subscribe(EventType.READING_FINISHED, new UnsetProgressLabel());

        EventManager.getInstance().subscribe(EventType.CRUNCHER_STARTED, new AddToCrunchingBox());
        EventManager.getInstance().subscribe(EventType.CRUNCHER_FINISHED, new RemoveFromCrunchingBox());

        EventManager.getInstance().subscribe(EventType.OUTPUT_STORED_CRUNCHER_DATA_FRAME, new AddToOutputView());
        EventManager.getInstance().subscribe(EventType.OUTPUT_GAINED_ACCESS_TO_CRUNCHER_DATA_FRAME, new UpdateOutputItem());

        EventManager.getInstance().subscribe(EventType.OUT_OF_MEMORY, new ForceExiting());

        Gui.show();

    }
}
