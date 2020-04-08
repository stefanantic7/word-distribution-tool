package rs.raf.word_distribution.file_input;

import rs.raf.word_distribution.Input;
import rs.raf.word_distribution.Utils;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class FileInput extends Input {

    private Disk disk;

    private List<File> dirs;

    private ExecutorService inputThreadPool;

    private Map<File, Long> lastModifiedMap;

    private Map<File, List<File>> filesByDirMap;

    private ReadingDiskWorker readingDiskWorker;

    public FileInput(Disk disc, ExecutorService inputThreadPool) {
        super();
        this.disk = disc;
        this.dirs = new CopyOnWriteArrayList<>();
        this.inputThreadPool = inputThreadPool;
        this.lastModifiedMap = new ConcurrentHashMap<>();
        this.filesByDirMap = new ConcurrentHashMap<>();

        this.readingDiskWorker = new ReadingDiskWorker(this);
        inputThreadPool.submit(readingDiskWorker);
    }

    public void addDir(File dir) {
        this.filesByDirMap.put(dir, new CopyOnWriteArrayList<>());
        this.dirs.add(dir);
    }

    public void removeDir(File dir) {
        filesByDirMap.get(dir).iterator().forEachRemaining(this.lastModifiedMap::remove);
        this.filesByDirMap.remove(dir);
        this.dirs.remove(dir);
    }

    @Override
    public void scan() {
        this.dirs.iterator().forEachRemaining(dir -> this.scanDirContent(dir, dir));
    }

    private void scanDirContent(File dir, File parentDir) {
        File[] files = dir.listFiles();

        if(files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                this.scanDirContent(file, parentDir);
            } else if (Utils.getExtension(file.getName()).equals("txt")) {
                this.filesByDirMap.get(parentDir).add(file);
                this.checkForReading(file);
            }
        }
    }

    private void checkForReading(File file)
    {
        Long lastModified = file.lastModified();
        Long modifiedAt = this.lastModifiedMap.put(file, lastModified);
        if (modifiedAt == null || !modifiedAt.equals(lastModified)) {
            this.dispatchReading(file);
        }
    }

    private void dispatchReading(File file) {
        System.out.println("Dispatching file to reading queue: " + file.getPath());

        try {
            this.disk.getReadingQueue().put(Optional.of(file));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        super.destroy();

        this.readingDiskWorker.destroy();
    }

    public Disk getDisk() {
        return disk;
    }

    public ExecutorService getInputThreadPool() {
        return inputThreadPool;
    }
}
