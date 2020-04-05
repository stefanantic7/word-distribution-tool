package rs.raf.word_distribution.gui.actions.input;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.Input;

public class ChooseCruncherAction implements EventHandler<ActionEvent> {

    private ReadOnlyObjectProperty<Cruncher<?, ?>> cruncherProperty;
    private Input input;
    private BooleanProperty linkCruncherButtonEnabledProperty;

    public ChooseCruncherAction(ObjectProperty<Cruncher<?, ?>> cruncherProperty,
                                Input input,
                                BooleanProperty linkCruncherButtonEnabledProperty) {
        this.cruncherProperty = cruncherProperty;
        this.input = input;
        this.linkCruncherButtonEnabledProperty = linkCruncherButtonEnabledProperty;
    }

    @Override
    public void handle(ActionEvent event) {
        Cruncher<?, ?> cruncher = this.cruncherProperty.get();
        if (cruncher == null) {
            this.linkCruncherButtonEnabledProperty.set(false);
            return;
        }

        this.linkCruncherButtonEnabledProperty.set(!this.input.getCrunchers().contains(cruncher));
    }
}
