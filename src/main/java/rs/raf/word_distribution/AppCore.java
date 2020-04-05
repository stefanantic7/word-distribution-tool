package rs.raf.word_distribution;

import javafx.application.Application;
import javafx.stage.Stage;
import rs.raf.word_distribution.cache_output.CacheOutput;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;
import rs.raf.word_distribution.counter_cruncher.CounterCruncher;
import rs.raf.word_distribution.events.EventManager;
import rs.raf.word_distribution.events.EventType;
import rs.raf.word_distribution.events.hanglers.*;
import rs.raf.word_distribution.file_input.Disk;
import rs.raf.word_distribution.file_input.FileInput;
import rs.raf.word_distribution.file_input.ReadingDiskWorker;
import rs.raf.word_distribution.gui.Gui;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

        EventManager.getInstance().subscribe(EventType.STORED_CRUNCHER_DATA_FRAME, new AddToOutputView());
        EventManager.getInstance().subscribe(EventType.CRUNCHER_FINISHED, new UpdateOutputItem());
        EventManager.getInstance().subscribe(EventType.AGGREGATION_FINISHED, new UpdateOutputItem());

        EventManager.getInstance().subscribe(EventType.OUT_OF_MEMORY, new ForceExiting());

        Gui.show();

    }
}
