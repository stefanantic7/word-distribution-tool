package rs.raf.word_distribution;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;

public class CruncherDataFrame<K, V> {
    private final String name;
    private final Future<Map<K, V>> future;

    public CruncherDataFrame(String name, Future<Map<K, V>> future) {
        this.name = name;
        this.future = future;
    }

    public String getName() {
        return name;
    }

    public Future<Map<K, V>> getFuture() {
        return future;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CruncherDataFrame<?, ?> that = (CruncherDataFrame<?, ?>) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(future, that.future);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, future);
    }

    @Override
    public String toString() {
        return "CruncherDataFrame{" +
                "name='" + name + '\'' +
                ", future=" + future +
                '}';
    }
}
