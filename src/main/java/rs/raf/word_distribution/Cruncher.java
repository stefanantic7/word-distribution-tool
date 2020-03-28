package rs.raf.word_distribution;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Cruncher implements Runnable {

    protected BlockingQueue<InputDataFrame> inputDataFrameBlockingQueue;

    protected int arity;

    protected List<Output> outputs;

    public Cruncher(int arity) {
        this.inputDataFrameBlockingQueue = new LinkedBlockingQueue<>();
        this.outputs = new CopyOnWriteArrayList<>();
        this.arity = arity;
    }

    public abstract void handle(InputDataFrame inputDataFrame);

    public void addOutput(Output output) {
        this.outputs.add(output);
    }

    public void removeOutput(Output output) {
        this.outputs.remove(output);
    }

    public int getArity() {
        return arity;
    }

    public void putInputDataFrame(InputDataFrame inputDataFrame) {
        this.inputDataFrameBlockingQueue.add(inputDataFrame);
    }

    @Override
    public void run() {
        while (true) {
            try {
                InputDataFrame inputDataFrame = inputDataFrameBlockingQueue.take();
                this.handle(inputDataFrame);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
