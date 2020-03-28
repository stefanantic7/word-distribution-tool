package rs.raf.word_distribution.file_input;

import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.Input;
import rs.raf.word_distribution.InputDataFrame;
import rs.raf.word_distribution.Utils;

import java.io.*;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class FileInput extends Input {

    private Disk disk;

    private List<File> folders;

    private ExecutorService inputThreadPool;

    private Map<File, Long> lastModifiedMap;

    private Map<File, List<File>> filesByFolderMap;

    public FileInput(Disk disc, ExecutorService inputThreadPool) {
        super();

        this.folders = new CopyOnWriteArrayList<>();
        this.lastModifiedMap = new ConcurrentHashMap<>();

        this.disk = disc;
        this.inputThreadPool = inputThreadPool;

        this.filesByFolderMap = new ConcurrentHashMap<>();
    }

    public void addFolder(File folder) {
        this.folders.add(folder);
    }

    public void removeFolder(File folder) {
        filesByFolderMap.get(folder).forEach(this.lastModifiedMap::remove);
        this.filesByFolderMap.remove(folder);
        this.folders.remove(folder);
    }

    @Override
    public void scan() {
        for (File file : this.folders) {
            this.filesByFolderMap.put(file, new CopyOnWriteArrayList<>());
            this.scanFolderContent(file);
        }
    }

    private void scanFolderContent(File folder) {
        File[] files = folder.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                this.scanFolderContent(file);
            } else if (Utils.getExtension(file.getName()).equals("txt")) {
                this.filesByFolderMap.get(folder).add(file);

                this.checkForReading(file);
            }
        }
    }

    private void checkForReading(File file)
    {
//        System.out.println("Checking file: " + file.getPath());

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
