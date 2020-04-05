package rs.raf.word_distribution;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Input implements Runnable {

    protected List<Cruncher<?, ?>> crunchers;

    protected AtomicBoolean running;

    protected AtomicBoolean destroyed;

    public Input() {
        this.crunchers = new CopyOnWriteArrayList<>();
        this.running = new AtomicBoolean(true);
        this.destroyed = new AtomicBoolean(false);
    }

    public void linkCruncher(Cruncher<?, ?> cruncher) {
        this.crunchers.add(cruncher);
    }

    public void unlinkCruncher(Cruncher<?, ?> cruncher) {
        this.crunchers.remove(cruncher);
    }

    public List<Cruncher<?, ?>> getCrunchers() {
        return crunchers;
    }

    public boolean isRunning() {
        return this.running.get();
    }

    public abstract void scan();

    public synchronized void pause() {
        if(this.running.compareAndSet(true, false)) {
            this.notify();
        }
    }

    public synchronized void start() {
        if(this.running.compareAndSet(false, true)) {
            this.notify();
        }
    }

    private synchronized void stop() {
        if(!this.running.get()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected synchronized void sleep(long millis) {
        if(this.running.get()) {
            try {
                this.wait(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void destroy() {
        this.notify();
        this.destroyed.set(true);
    }

    @Override
    public void run() {
        while (!this.destroyed.get()) {
            if (this.running.get()) {
                this.scan();

                this.sleep(Config.FILE_INPUT_SLEEP_TIME);
            } else {
                this.stop();
            }
        }
    }
}
