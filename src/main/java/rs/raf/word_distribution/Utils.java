package rs.raf.word_distribution;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class Utils {

    public static String getExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        return Optional.of(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1))
                .get();
    }
}
