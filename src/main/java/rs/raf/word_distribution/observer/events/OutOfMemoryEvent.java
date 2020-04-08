package rs.raf.word_distribution.observer.events;

import rs.raf.word_distribution.observer.Event;

public class OutOfMemoryEvent extends Event {
    OutOfMemoryError outOfMemoryError;

    public OutOfMemoryEvent(OutOfMemoryError outOfMemoryError) {
        this.outOfMemoryError = outOfMemoryError;
    }

    public OutOfMemoryError getOutOfMemoryError() {
        return outOfMemoryError;
    }
}
