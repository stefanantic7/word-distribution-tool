package rs.raf.word_distribution;

import java.util.HashMap;
import java.util.Objects;

public class CruncherDataFrame {
    private final String name;
    private final Object data;

    public CruncherDataFrame(String name, Object data) {
        this.name = name;
        this.data = data;
    }

    private CruncherDataFrame(String name, Object data, boolean completed) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public Object getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CruncherDataFrame that = (CruncherDataFrame) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }

    @Override
    public String toString() {
        return "CruncherDataFrame{" +
                "name='" + name + '\'' +
                ", data=" + data +
                '}';
    }
}
