package rs.raf.word_distribution.gui.views;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import rs.raf.word_distribution.Cruncher;
import rs.raf.word_distribution.gui.views.cruncher.CrunchersView;
import rs.raf.word_distribution.gui.views.input.FileInputsView;
import rs.raf.word_distribution.gui.views.output.ChartResultView;
import rs.raf.word_distribution.gui.views.output.OutputView;

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
