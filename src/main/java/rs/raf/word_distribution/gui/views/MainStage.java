package rs.raf.word_distribution.gui.views;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import rs.raf.word_distribution.AppCore;
import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.Input;
import rs.raf.word_distribution.gui.views.cruncher.CrunchersView;
import rs.raf.word_distribution.gui.views.input.FileInputsView;
import rs.raf.word_distribution.gui.views.output.ChartResultView;
import rs.raf.word_distribution.gui.views.output.OutputView;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainStage extends Stage {

    private static MainStage instance;

    public static MainStage getInstance() {
        if (instance == null) {
            synchronized (MainStage.class) {
                if(instance==null) {
                    instance = new MainStage();
                }
            }
        }
        return instance;
    }

    private FileInputsView fileInputsView;
    private CrunchersView crunchersView;
    private OutputView outputView;
    private ChartResultView chartResultView;

    private MainStage() {
        this.init();
    }

    private void init() {

        this.outputView = new OutputView();
        this.crunchersView = new CrunchersView(outputView.getOutput());
        this.fileInputsView = new FileInputsView(crunchersView.getCruncherObservableList());
        this.chartResultView = new ChartResultView();

        HBox fileInputsAndCrunchersHBox = new HBox(5, fileInputsView, crunchersView);

        BorderPane borderPane = new BorderPane();

        borderPane.setLeft(fileInputsAndCrunchersHBox);
        borderPane.setCenter(chartResultView);
        borderPane.setRight(outputView);

        Scene scene = new Scene(borderPane);
        this.setScene(scene);

        this.setHeight(Screen.getPrimary().getBounds().getHeight());
        this.setWidth(Screen.getPrimary().getBounds().getWidth());
        this.show();

        this.setOnCloseRequest(e -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Okay, the program will be closed soon");
            alert.setContentText("Please wait...");
            alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
            alert.initStyle(StageStyle.UNDECORATED);


            Thread t = new Thread(() -> {
                AppCore.getInputThreadPool().shutdown();
                AppCore.getOutputThreadPool().shutdown();

                this.fileInputsView.getFileInputsSet().forEach(Input::destroy);
                this.crunchersView.getCruncherObservableList().forEach(Cruncher::destroy);
                this.outputView.getOutput().destroy();

                AppCore.getCruncherThreadPool().awaitQuiescence(365, TimeUnit.DAYS);

                try {
                    AppCore.getInputThreadPool().awaitTermination(365, TimeUnit.DAYS);
                    AppCore.getOutputThreadPool().awaitTermination(365, TimeUnit.DAYS);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                Platform.runLater(alert::close);

            });
            t.start();

            alert.showAndWait();

        });
    }

    public FileInputsView getFileInputsView() {
        return fileInputsView;
    }

    public CrunchersView getCrunchersView() {
        return crunchersView;
    }

    public OutputView getOutputView() {
        return outputView;
    }

    public ChartResultView getChartResultView() {
        return chartResultView;
    }
}
