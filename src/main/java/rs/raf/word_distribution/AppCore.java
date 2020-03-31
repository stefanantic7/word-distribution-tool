package rs.raf.word_distribution;

import javafx.stage.Stage;
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

    public static void main(String[] args) {
        ExecutorService inputThreadPool = Executors.newCachedThreadPool();
        ForkJoinPool cruncherThreadPool = ForkJoinPool.commonPool();
        ExecutorService outputThreadPool = Executors.newCachedThreadPool();


        Disk disk1 = new Disk(Config.DISKS.get(0));
//        Disk disk2 = new Disk(Config.DISKS.get(1));
        FileInput input1 = new FileInput(disk1, inputThreadPool);
//        FileInput input2 = new FileInput(disk2, inputThreadPool);

        Cruncher cruncher = new CounterCruncher(3, cruncherThreadPool);
//        Cruncher cruncher2 = new CounterCruncher(2, cruncherThreadPool);
        input1.linkCruncher(cruncher);
//        input1.linkCruncher(cruncher2);
//        input2.linkCruncher(cruncher);

        Thread cruncherThread = new Thread(cruncher);
//        Thread cruncherThread2 = new Thread(cruncher2);
        cruncherThread.start();
//        cruncherThread2.start();

        Output output = new CacheOutput(outputThreadPool);
        cruncher.linkOutputs(output);
//        cruncher2.linkOutputs(output);

        Thread outputThread = new Thread(output);
        outputThread.start();

        File folderA = new File(disk1.getDiskPath() + "A");
//        File folderB = new File(disk1.getDiskPath() + "B");
//        File folderC = new File(disk2.getDiskPath() + "C");
//        File folderD = new File(disk2.getDiskPath() + "D");
        input1.addFolder(folderA);
//        input1.addFolder(folderB);
//        input2.addFolder(folderC);
//        input2.addFolder(folderD);

        Thread input1Thread = new Thread(input1);
//        Thread input2Thread = new Thread(input2);
        input1Thread.start();
//        input2Thread.start();

        Thread readingFileWorkerDisk1 = new Thread(new ReadingDiskWorker(input1));
//        Thread readingFileWorkerDisk2 = new Thread(new ReadingDiskWorker(input2));
        readingFileWorkerDisk1.start();
//        readingFileWorkerDisk2.start();





//        BagOfWords b1 = new BagOfWords(2);
//        b1.add(new String("str1"));
//        b1.add(new String("str2"));
//
//        BagOfWords b2 = new BagOfWords(2);
//        b2.add(new String("str2"));
//        b2.add(new String("str1"));
//
//        System.out.println(b1);
//        System.out.println(b2);
//
//        System.out.println(b1.hashCode());
//        System.out.println(b2.hashCode());
//
//        System.out.println(b1.equals(b2));
    }
}
