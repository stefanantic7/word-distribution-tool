package rs.raf.word_distribution;

import java.util.Optional;

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
