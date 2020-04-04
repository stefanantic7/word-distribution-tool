package rs.raf.word_distribution.events;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EventManager {
    private static EventManager instance;

    public static EventManager getInstance() {
        if (instance == null) {
            synchronized (EventManager.class) {
                if(instance==null) {
                    instance = new EventManager();
                }
            }
        }
        return instance;
    }

    private Map<EventType, List<EventListener>> eventToListenersMap;

    private EventManager() {
        this.eventToListenersMap = new HashMap<>();
    }

    public void subscribe(EventType eventType, EventListener eventListener) {
        eventToListenersMap.computeIfAbsent(eventType, k -> new LinkedList<>());
        eventToListenersMap.get(eventType).add(eventListener);
    }

    public void unsubscribe(EventType eventType, EventListener eventListener) {
        if (eventToListenersMap.get(eventType) == null) {
            return;
        }
        eventToListenersMap.get(eventType).remove(eventListener);
    }

    public void notify(EventType eventType, Object... args) {
        if (eventToListenersMap.get(eventType) == null) {
            return;
        }
        eventToListenersMap.get(eventType).forEach(eventListener -> {
            eventListener.handleEvent(eventType, args);
        });
    }
}
