package rs.raf.word_distribution.observer.events;

import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.observer.Event;

public class CruncherStartedEvent extends Event {

    protected Cruncher<?, ?> cruncher;
    protected CruncherDataFrame<?, ?> cruncherDataFrame;

    public CruncherStartedEvent(Cruncher<?, ?> cruncher, CruncherDataFrame<?, ?> cruncherDataFrame) {
        this.cruncher = cruncher;
        this.cruncherDataFrame = cruncherDataFrame;
    }

    public Cruncher<?, ?> getCruncher() {
        return cruncher;
    }

    public CruncherDataFrame<?, ?> getCruncherDataFrame() {
        return cruncherDataFrame;
    }
}
