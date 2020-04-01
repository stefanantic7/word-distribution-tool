package rs.raf.word_distribution.file_input;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Disk {
    private final String diskPath;

    private final BlockingQueue<File> readingQueue;

    public Disk(String diskPath) {
        this.diskPath = diskPath;

        this.readingQueue = new LinkedBlockingQueue<>();
    }

    public String getDiskPath() {
        return diskPath;
    }

    public String getAbsolutePathOfDir(String dir) {
        return this.getDiskPath() + dir;
    }

    public BlockingQueue<File> getReadingQueue() {
        return readingQueue;
    }
}
