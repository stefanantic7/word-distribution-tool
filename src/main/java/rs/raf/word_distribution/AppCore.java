package rs.raf.word_distribution;

import rs.raf.word_distribution.client.Client;

import java.util.concurrent.*;

public class AppCore {

    /**
     * COMPONENT THREAD POOLS
     */
    // For each disk, there is another thread for reading.
    private static final ExecutorService inputThreadPool = Executors.newFixedThreadPool(Config.DISKS.size()*2);
    private static final ExecutorService cruncherThreadPool = Executors.newCachedThreadPool();
    private static final ExecutorService outputThreadPool = Executors.newFixedThreadPool(1);

    /**
     * TASKS THREAD POOLS
     */
    private static final ExecutorService inputTasksThreadPool = Executors.newCachedThreadPool();
    private static final ForkJoinPool cruncherTasksThreadPool = ForkJoinPool.commonPool();
//    private static final ForkJoinPool cruncherTasksThreadPool = new ForkJoinPool(4);
    private static final ExecutorService outputTasksThreadPool = Executors.newCachedThreadPool();

    public static ExecutorService getInputThreadPool() {
        return inputThreadPool;
    }
    public static ExecutorService getCruncherThreadPool() {
        return cruncherThreadPool;
    }
    public static ExecutorService getOutputThreadPool() {
        return outputThreadPool;
    }

    public static ExecutorService getInputTasksThreadPool() {
        return inputTasksThreadPool;
    }
    public static ForkJoinPool getCruncherTasksThreadPool() {
        return cruncherTasksThreadPool;
    }
    public static ExecutorService getOutputTasksThreadPool() {
        return outputTasksThreadPool;
    }

    public static void main(String[] args) {
        Client.initialize();
    }
}
