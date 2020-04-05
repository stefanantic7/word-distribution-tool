package rs.raf.word_distribution.gui.views.cruncher;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CrunchingListView extends VBox {

    private ObservableList<String> items;

    private Map<String, Boolean> removed;

    public CrunchingListView() {
        this(FXCollections.observableArrayList());
    }

    public CrunchingListView(ObservableList<String> items) {
        this.items = items;
        this.removed = new ConcurrentHashMap<>();

        this.init();
    }

    public void init() {
        this.setVisible(false);
        Label crunchingLabel = new Label("Crunching...");
        this.getChildren().add(crunchingLabel);

        VBox itemsBox = new VBox();
        items.addListener((ListChangeListener<String>) c -> {
            c.next();
            itemsBox.getChildren().clear();
            for (String item: items) {
                itemsBox.getChildren().add(new Label(item));
            }

            if(items.size() > 0) {
                this.setVisible(true);
            } else {
                this.setVisible(false);
            }
        });

        this.getChildren().add(itemsBox);
    }

    public synchronized void add(String item) {
        Boolean isRemoved = this.removed.get(item);
        if (isRemoved == null) {
            this.items.add(item);
        }
    }

    public synchronized void remove(String item) {
        this.removed.put(item, true);
        this.items.remove(item);
    }


}
