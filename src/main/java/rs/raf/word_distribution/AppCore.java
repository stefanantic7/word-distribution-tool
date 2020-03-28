package rs.raf.word_distribution;

import rs.raf.word_distribution.counter_cruncher.BagOfWords;
import rs.raf.word_distribution.counter_cruncher.CounterCruncher;
import rs.raf.word_distribution.file_input.Disk;
import rs.raf.word_distribution.file_input.FileInput;
import rs.raf.word_distribution.file_input.ReadingFileWorker;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppCore {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService inputThreadPool = Executors.newCachedThreadPool();
        ExecutorService cruncherThreadPool = Executors.newCachedThreadPool();

        Disk disk1 = new Disk("/Users/antic/Dev/faks/word-distribution-tool/data/disk1/");
        Disk disk2 = new Disk("/Users/antic/Dev/faks/word-distribution-tool/data/disk2/");
        FileInput input1 = new FileInput(disk1, inputThreadPool);
        FileInput input2 = new FileInput(disk2, inputThreadPool);

        Cruncher cruncher = new CounterCruncher(1, cruncherThreadPool);
        input1.linkCruncher(cruncher);
        input2.linkCruncher(cruncher);

        Thread cruncherThread = new Thread(cruncher);
//        cruncherThread.start();

        File folderA = new File(disk1.getDiskPath() + "A");
        File folderB = new File(disk1.getDiskPath() + "B");
        File folderC = new File(disk2.getDiskPath() + "C");
        File folderD = new File(disk2.getDiskPath() + "D");
        input1.addFolder(folderA);
        input1.addFolder(folderB);
        input2.addFolder(folderC);
        input2.addFolder(folderD);

        Thread input1Thread = new Thread(input1);
        Thread input2Thread = new Thread(input2);
        input1Thread.start();
        input2Thread.start();

        Thread readingFileWorkerDisk1 = new Thread(new ReadingFileWorker(input1));
        Thread readingFileWorkerDisk2 = new Thread(new ReadingFileWorker(input2));
        readingFileWorkerDisk1.start();
        readingFileWorkerDisk2.start();
    }
}
