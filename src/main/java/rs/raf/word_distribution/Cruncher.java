package rs.raf.word_distribution;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Cruncher<K, V> implements Runnable {

    protected BlockingQueue<Optional<InputDataFrame>> inputDataFrameBlockingQueue;

    protected List<Output<K, V>> outputs;

    public Cruncher() {
        this.inputDataFrameBlockingQueue = new LinkedBlockingQueue<>();
        this.outputs = new CopyOnWriteArrayList<>();
    }

    public abstract void handle(InputDataFrame inputDataFrame);

    public void addOutput(Output<K, V> output) {
        this.outputs.add(output);
    }

    public void removeOutput(Output<K, V> output) {
        this.outputs.remove(output);
    }

    public void linkOutputs(Output<K, V> output) {
        this.outputs.add(output);
    }

    public List<Output<K, V>> getOutputs() {
        return outputs;
    }

    public void broadcastInputDataFrame(InputDataFrame inputDataFrame) {
        this.inputDataFrameBlockingQueue.add(Optional.of(inputDataFrame));
    }

    @Override
    public void run() {
        while (true) {
            try {
                Optional<InputDataFrame> inputDataFrameOptional = inputDataFrameBlockingQueue.take();
                if (inputDataFrameOptional.isEmpty()) {
                    break;
                }
                InputDataFrame inputDataFrame = inputDataFrameOptional.get();

                System.out.println("Cruncher will handle: " + inputDataFrame.getSource());
                this.handle(inputDataFrame);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void destroy() {
        try {
            inputDataFrameBlockingQueue.put(Optional.ofNullable(null));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Cruncher{" +
                "inputDataFrameBlockingQueue=" + inputDataFrameBlockingQueue +
                ", outputs=" + outputs +
                '}';
    }
}
