package rs.raf.word_distribution.file_input;

import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Disk {
    private final String diskPath;

    private final BlockingQueue<Optional<File>> readingQueue;

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

    public BlockingQueue<Optional<File>> getReadingQueue() {
        return readingQueue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disk disk = (Disk) o;
        return Objects.equals(diskPath, disk.diskPath) &&
                Objects.equals(readingQueue, disk.readingQueue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diskPath, readingQueue);
    }

    @Override
    public String toString() {
        return "Disk: " + diskPath;
    }
}
