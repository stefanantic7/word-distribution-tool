package rs.raf.word_distribution.gui.views.output;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import rs.raf.word_distribution.CruncherDataFrame;

public class OutputListItem {
    private String title;

    private String originalName;

    public OutputListItem(String originalName) {
        this.originalName = originalName;
        this.title = originalName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
