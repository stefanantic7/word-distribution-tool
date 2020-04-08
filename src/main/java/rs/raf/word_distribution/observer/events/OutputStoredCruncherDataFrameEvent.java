package rs.raf.word_distribution.observer.events;

import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.observer.Event;

public class OutputStoredCruncherDataFrameEvent extends Event {

    protected CruncherDataFrame<?, ?> cruncherDataFrame;

    public OutputStoredCruncherDataFrameEvent(CruncherDataFrame<?, ?> cruncherDataFrame) {
        this.cruncherDataFrame = cruncherDataFrame;
    }

    public CruncherDataFrame<?, ?> getCruncherDataFrame() {
        return cruncherDataFrame;
    }
}
