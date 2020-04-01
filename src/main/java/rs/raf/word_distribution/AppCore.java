package rs.raf.word_distribution;

import rs.raf.word_distribution.cache_output.CacheOutput;
import rs.raf.word_distribution.counter_cruncher.BagOfWords;
import rs.raf.word_distribution.counter_cruncher.CounterCruncher;
import rs.raf.word_distribution.file_input.Disk;
import rs.raf.word_distribution.file_input.FileInput;
import rs.raf.word_distribution.file_input.ReadingDiskWorker;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class AppCore {

    public static void main(String[] args) {
        ExecutorService inputThreadPool = Executors.newCachedThreadPool();
        ForkJoinPool cruncherThreadPool = ForkJoinPool.commonPool();
        ExecutorService outputThreadPool = Executors.newCachedThreadPool();

        Disk disk1 = new Disk(Config.DISKS.get(0));
//        Disk disk2 = new Disk(Config.DISKS.get(1));
        FileInput input1 = new FileInput(disk1, inputThreadPool);
//        FileInput input2 = new FileInput(disk2, inputThreadPool);

        Cruncher<BagOfWords, Integer> cruncher = new CounterCruncher(1, cruncherThreadPool);
//        Cruncher cruncher2 = new CounterCruncher(2, cruncherThreadPool);
        input1.linkCruncher(cruncher);
//        input1.linkCruncher(cruncher2);
//        input2.linkCruncher(cruncher);

        Thread cruncherThread = new Thread(cruncher);
//        Thread cruncherThread2 = new Thread(cruncher2);
        cruncherThread.start();
//        cruncherThread2.start();

        Output<BagOfWords, Integer> output = new CacheOutput<>(outputThreadPool);
        cruncher.linkOutputs(output);
//        cruncher2.linkOutputs(output);

        Thread outputThread = new Thread(output);
        outputThread.start();

        File folderA = new File(disk1.getAbsolutePathOfDir("A"));
//        File folderB = new File(disk1.getAbsolutePathOfDir("B"));
//        File folderC = new File(disk2.getAbsolutePathOfDir("C"));
//        File folderD = new File(disk2.getAbsolutePathOfDir("D"));
        input1.addDir(folderA);
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





        //// Aggregating test
//        CacheOutput<BagOfWords, Integer> cacheOutput = new CacheOutput<>(Executors.newCachedThreadPool());
//
//        ExecutorService pool = Executors.newCachedThreadPool();
//
//        cacheOutput.store("n0", pool.submit( () -> {
//            Map<BagOfWords, Integer> map = new HashMap<>();
//            BagOfWords b1 = new BagOfWords(2);
//            b1.add("rec0");
//            b1.add("rec0");
//
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            map.put(b1, 1);
//            return map;
//        }));
//        cacheOutput.store("n1", pool.submit( () -> {
//            Map<BagOfWords, Integer> map = new HashMap<>();
//            BagOfWords b1 = new BagOfWords(2);
//            b1.add("rec1");
//            b1.add("rec2");
//
//            map.put(b1, 2);
//            return map;
//        }));
//
//        cacheOutput.store("n2", pool.submit( () -> {
//            Map<BagOfWords, Integer> map = new HashMap<>();
//            BagOfWords b1 = new BagOfWords(2);
//            b1.add("rec1");
//            b1.add("rec2");
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            map.put(b1, 1);
//            return map;
//        }));
//
//        ArrayList<String> existing = new ArrayList<>();
//        existing.add("n1");
//        existing.add("n2");
//        existing.add("n0");
//        cacheOutput.aggregate("n3", existing, Integer::sum);
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(cacheOutput.take("n3"));

    }
}
