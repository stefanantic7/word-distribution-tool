package rs.raf.word_distribution.gui.actions.input;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.Input;

public class LinkCruncherAction implements EventHandler<ActionEvent> {

    private ObjectProperty<Cruncher<?, ?>> cruncherProperty;
    private BooleanProperty linkCruncherButtonEnabledProperty;
    private Input input;
    private ObjectProperty<ObservableList<Cruncher<?, ?>>> crunchersList;

    public LinkCruncherAction(ObjectProperty<Cruncher<?, ?>> cruncherProperty,
                              BooleanProperty linkCruncherButtonEnabledProperty,
                              Input input,
                              ObjectProperty<ObservableList<Cruncher<?, ?>>> crunchersList) {
        this.cruncherProperty = cruncherProperty;
        this.linkCruncherButtonEnabledProperty = linkCruncherButtonEnabledProperty;
        this.input = input;
        this.crunchersList = crunchersList;
    }

    @Override
    public void handle(ActionEvent event) {
        Cruncher<?, ?> cruncher = cruncherProperty.getValue();

        this.linkCruncherButtonEnabledProperty.set(false);
        this.input.linkCruncher(cruncher);
        this.crunchersList.get().add(cruncher);
    }
}
