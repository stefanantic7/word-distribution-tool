package rs.raf.word_distribution.observer;

abstract public class Listener<T extends Event> {

    protected abstract void handleEvent(T event);

    public void handle(Event event) {
        this.handleEvent((T) event);
    }
}
