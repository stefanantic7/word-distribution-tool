package rs.raf.word_distribution;

import rs.raf.word_distribution.cache_output.CacheOutput;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;
import rs.raf.word_distribution.counter_cruncher.CounterCruncher;
import rs.raf.word_distribution.file_input.Disk;
import rs.raf.word_distribution.file_input.FileInput;
import rs.raf.word_distribution.file_input.ReadingDiskWorker;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class AppCore {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService inputThreadPool = Executors.newCachedThreadPool();
//        ExecutorService inputThreadPool = Executors.newFixedThreadPool(2);
        ForkJoinPool cruncherThreadPool = new ForkJoinPool();
//        ExecutorService cruncherThreadPool = Executors.newCachedThreadPool();
//        ExecutorService cruncherThreadPool = Executors.newFixedThreadPool(1);

        Disk disk1 = new Disk(Config.DISKS.get(0));
        Disk disk2 = new Disk(Config.DISKS.get(1));
        FileInput input1 = new FileInput(disk1, inputThreadPool);
        FileInput input2 = new FileInput(disk2, inputThreadPool);

        Cruncher cruncher = new CounterCruncher(1, cruncherThreadPool);
        input1.linkCruncher(cruncher);
        input2.linkCruncher(cruncher);

        Thread cruncherThread = new Thread(cruncher);
        cruncherThread.start();

        Output output = new CacheOutput();
        cruncher.linkOutputs(output);

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

        Thread readingFileWorkerDisk1 = new Thread(new ReadingDiskWorker(input1));
        Thread readingFileWorkerDisk2 = new Thread(new ReadingDiskWorker(input2));
        readingFileWorkerDisk1.start();
        readingFileWorkerDisk2.start();

    }
}
