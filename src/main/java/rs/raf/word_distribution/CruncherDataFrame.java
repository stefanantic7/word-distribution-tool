package rs.raf.word_distribution;

import java.util.HashMap;

public class CruncherDataFrame<T> {
    private final String name;
    private final T data;

    public CruncherDataFrame(String name, T data) {
        this.name = name;
        this.data = data;
    }
}
