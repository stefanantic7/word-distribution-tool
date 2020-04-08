package rs.raf.word_distribution.observer;

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

    private Map<Class<? extends Event>, List<Listener<?>>> eventToListenersMap;

    private EventManager() {
        this.eventToListenersMap = new HashMap<>();
    }

    public void subscribe(Class<? extends Event> event, Listener<?> eventListener) {
        eventToListenersMap.computeIfAbsent(event, k -> new LinkedList<>());
        eventToListenersMap.get(event).add(eventListener);
    }

    public void unsubscribe(Class<? extends Event> event, Listener<?> eventListener) {
        if (eventToListenersMap.get(event) == null) {
            return;
        }
        eventToListenersMap.get(event).remove(eventListener);
    }

    public void notify(Event event) {
        if (eventToListenersMap.get(event.getClass()) == null) {
            return;
        }
        eventToListenersMap.get(event.getClass()).forEach(eventListener -> {
            eventListener.handle(event);
        });
    }
}
