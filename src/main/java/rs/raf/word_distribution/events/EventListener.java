package rs.raf.word_distribution.events;

public interface EventListener {
    void handleEvent(EventType eventType, Object... args);
}
