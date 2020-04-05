package rs.raf.word_distribution.events.hanglers;

import rs.raf.word_distribution.events.EventListener;
import rs.raf.word_distribution.events.EventType;

public class ForceExiting implements EventListener {
    @Override
    public void handleEvent(EventType eventType, Object... args) {
        System.exit(1);
    }
}
