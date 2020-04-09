package rs.raf.word_distribution.client.actions;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import rs.raf.word_distribution.AppCore;
import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.Input;
import rs.raf.word_distribution.client.views.MainStage;

import java.util.concurrent.TimeUnit;

public class CloseApplicationThread implements Runnable {

    private Alert alert;

    public CloseApplicationThread(Alert alert) {
        this.alert = alert;
    }

    @Override
    public void run() {
        MainStage.getInstance().getFileInputsView().getFileInputsSet().forEach(Input::destroy);
        MainStage.getInstance().getCrunchersView().getCruncherObservableList().forEach(Cruncher::destroy);
        MainStage.getInstance().getOutputView().getOutput().destroy();

        AppCore.getInputThreadPool().shutdown();
        AppCore.getCruncherThreadPool().shutdown();
        AppCore.getOutputThreadPool().shutdown();

        AppCore.getInputTasksThreadPool().shutdown();
        AppCore.getOutputTasksThreadPool().shutdown();

        AppCore.getCruncherTasksThreadPool().awaitQuiescence(365, TimeUnit.DAYS);

        try {
            AppCore.getInputThreadPool().awaitTermination(365, TimeUnit.DAYS);
            AppCore.getCruncherThreadPool().awaitTermination(365, TimeUnit.DAYS);
            AppCore.getOutputThreadPool().awaitTermination(365, TimeUnit.DAYS);

            AppCore.getInputTasksThreadPool().awaitTermination(365, TimeUnit.DAYS);
            AppCore.getOutputTasksThreadPool().awaitTermination(365, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        Platform.runLater(this.alert::close);
    }
}
