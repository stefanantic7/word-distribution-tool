package rs.raf.word_distribution.counter_cruncher;

import rs.raf.word_distribution.CruncherDataFrame;
import rs.raf.word_distribution.InputDataFrame;
import rs.raf.word_distribution.Output;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class WordDistributor implements Runnable {

    private CounterCruncher counterCruncher;
    private InputDataFrame inputDataFrame;

    public WordDistributor(InputDataFrame inputDataFrame, CounterCruncher counterCruncher) {
        this.inputDataFrame = inputDataFrame;
        this.counterCruncher = counterCruncher;
    }

    @Override
    public void run() {
        CruncherDataFrame<BagOfWords, Integer> cruncherDataFrame = this.generateCruncherDataFrame();

        this.broadcastCruncherDataFrame(cruncherDataFrame);

        // TODO: remove gc
//        try {
//            cruncherDataFrame.getFuture().get();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//        System.gc();
    }

    private CruncherDataFrame<BagOfWords, Integer> generateCruncherDataFrame() {
        String dataFrameName = this.generateDataFrameName();

        String content = this.inputDataFrame.getContent();

        WordCounterTask wordCounterTask
                = new WordCounterTask(0, content.length(), content, this.counterCruncher.getArity());

        Future<Map<BagOfWords, Integer>> futureResult
                = this.counterCruncher.getCruncherThreadPool().submit(wordCounterTask);

        return new CruncherDataFrame<>(dataFrameName, futureResult);
    }

    private String generateDataFrameName() {
        return inputDataFrame.getSource() + "-arity"+this.counterCruncher.getArity();
    }

    private void broadcastCruncherDataFrame(CruncherDataFrame<BagOfWords, Integer> cruncherDataFrame) {
        for (Output<BagOfWords, Integer> output : this.counterCruncher.getOutputs()) {
            System.out.println("Broadcasting to outputs: "+cruncherDataFrame.getName());

            output.putCruncherDataFrame(cruncherDataFrame);
        }
    }
}
