package rs.raf.word_distribution.observer.events;

import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.observer.Event;

public class OutputGainedAccessToCruncherDataFrame extends Event {

    protected CruncherDataFrame<?, ?> cruncherDataFrame;

    public OutputGainedAccessToCruncherDataFrame(CruncherDataFrame<?, ?> cruncherDataFrame) {
        this.cruncherDataFrame = cruncherDataFrame;
    }

    public CruncherDataFrame<?, ?> getCruncherDataFrame() {
        return cruncherDataFrame;
    }
}
