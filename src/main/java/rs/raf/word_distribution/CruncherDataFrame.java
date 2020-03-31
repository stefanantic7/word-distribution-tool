package rs.raf.word_distribution;

import java.util.HashMap;
import java.util.Objects;

public class CruncherDataFrame {
    private final String name;
    private final Object data;
    private final boolean completed;

    public CruncherDataFrame(String name, Object data) {
        this.name = name;
        this.data = data;
        this.completed = false;
    }

    private CruncherDataFrame(String name, Object data, boolean completed) {
        this.name = name;
        this.data = data;
        this.completed = completed;
    }

    public String getName() {
        return name;
    }

    public Object getData() {
        return data;
    }

    public boolean isCompleted() {
        return completed;
    }

    public CruncherDataFrame complete(Object data) {
        return new CruncherDataFrame(this.getName(), data, true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CruncherDataFrame that = (CruncherDataFrame) o;
        return completed == that.completed &&
                Objects.equals(name, that.name) &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data, completed);
    }

    @Override
    public String toString() {
        return "CruncherDataFrame{" +
                "name='" + name + '\'' +
                ", data=" + data +
                ", completed=" + completed +
                '}';
    }
}
