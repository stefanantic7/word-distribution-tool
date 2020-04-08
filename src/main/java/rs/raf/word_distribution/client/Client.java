package rs.raf.word_distribution.client;

import javafx.application.Application;
import javafx.stage.Stage;
import rs.raf.word_distribution.client.views.MainStage;
import rs.raf.word_distribution.observer.EventManager;
import rs.raf.word_distribution.client.event_hanglers.*;
import rs.raf.word_distribution.observer.events.*;

public class Client extends Application {

    public static void initialize() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.subscribeToEvents();
        MainStage.getInstance();
    }

    private void subscribeToEvents() {
        EventManager eventManager = EventManager.getInstance();

        eventManager.subscribe(ReadingStartedEvent.class, new SetProgressLabel());
        eventManager.subscribe(ReadingFinishedEvent.class, new UnsetProgressLabel());

        eventManager.subscribe(CruncherStartedEvent.class, new AddToCrunchingBox());
        eventManager.subscribe(CruncherFinishedEvent.class, new RemoveFromCrunchingBox());

        eventManager.subscribe(OutputStoredCruncherDataFrameEvent.class, new AddToOutputView());
        eventManager.subscribe(OutputGainedAccessToCruncherDataFrame.class, new UpdateOutputItem());

        eventManager.subscribe(OutOfMemoryEvent.class, new ForceExiting());


    }
}
