package rs.raf.word_distribution;

import rs.raf.word_distribution.client.Client;

import java.util.concurrent.*;

public class AppCore {

    private static final ExecutorService inputThreadPool = Executors.newCachedThreadPool();

    private static final ForkJoinPool cruncherThreadPool = ForkJoinPool.commonPool();
//    private static final ForkJoinPool cruncherThreadPool = new ForkJoinPool(4);

    private static final ExecutorService outputThreadPool = Executors.newCachedThreadPool();

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
        Client.initialize();
    }
}
