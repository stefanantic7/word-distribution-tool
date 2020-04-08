package rs.raf.word_distribution.client.actions.input;

import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.Input;

public class UnlinkCruncherAction implements EventHandler<ActionEvent> {

    private ListView<Cruncher<?, ?>> cruncherListView;
    private Input input;
    private ComboBox<Cruncher<?, ?>> cruncherComboBox;
    private BooleanProperty linkCruncherButtonEnabledProperty;

    public UnlinkCruncherAction(ListView<Cruncher<?, ?>> cruncherListView,
                                Input input,
                                ComboBox<Cruncher<?, ?>> cruncherComboBox,
                                BooleanProperty linkCruncherButtonEnabledProperty) {
        this.cruncherListView = cruncherListView;
        this.input = input;
        this.cruncherComboBox = cruncherComboBox;
        this.linkCruncherButtonEnabledProperty = linkCruncherButtonEnabledProperty;
    }

    @Override
    public void handle(ActionEvent event) {
        Cruncher<?, ?> cruncher = this.cruncherListView.getSelectionModel().getSelectedItem();
        this.input.unlinkCruncher(cruncher);
        this.cruncherListView.getItems().remove(cruncher);

        if(this.cruncherComboBox.getValue() == cruncher) {
            this.linkCruncherButtonEnabledProperty.set(true);
        }
    }
}
