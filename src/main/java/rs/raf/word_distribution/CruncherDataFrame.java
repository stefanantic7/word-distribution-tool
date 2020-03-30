package rs.raf.word_distribution;

import java.util.HashMap;
import java.util.Objects;

public class CruncherDataFrame<T> {
    private final String name;
    private final T data;
    private final boolean completed;

    public CruncherDataFrame(String name, T data) {
        this.name = name;
        this.data = data;
        this.completed = false;
    }

    private CruncherDataFrame(String name, T data, boolean completed) {
        this.name = name;
        this.data = data;
        this.completed = completed;
    }

    public String getName() {
        return name;
    }

    public T getData() {
        return data;
    }

    public boolean isCompleted() {
        return completed;
    }

    public CruncherDataFrame<T> complete(T data) {
        return new CruncherDataFrame<>(this.getName(), data, true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CruncherDataFrame<?> that = (CruncherDataFrame<?>) o;
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
