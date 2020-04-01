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

    public FileInput(Disk disc, ExecutorService inputThreadPool) {
        super();
        this.disk = disc;
        this.dirs = new CopyOnWriteArrayList<>();
        this.inputThreadPool = inputThreadPool;
        this.lastModifiedMap = new ConcurrentHashMap<>();
        this.filesByDirMap = new ConcurrentHashMap<>();
    }

    public void addDir(File dir) {
        this.dirs.add(dir);
    }

    public void removeDir(File dir) {
        filesByDirMap.get(dir).forEach(this.lastModifiedMap::remove);
        this.filesByDirMap.remove(dir);
        this.dirs.remove(dir);
    }

    @Override
    public void scan() {
        for (File file : this.dirs) {
            this.filesByDirMap.put(file, new CopyOnWriteArrayList<>());
            this.scanDirContent(file);
        }
    }

    private void scanDirContent(File dir) {
        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                this.scanDirContent(file);
            } else if (Utils.getExtension(file.getName()).equals("txt")) {
                this.filesByDirMap.get(dir).add(file);
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
        System.out.println("Dispatching file: " + file.getPath());

        try {
            this.disk.getReadingQueue().put(file);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Disk getDisk() {
        return disk;
    }

    public ExecutorService getInputThreadPool() {
        return inputThreadPool;
    }
}
