package HW_3_ThreadPool;

public class FixedThreadPool extends ThreadPoolBasics {
    private final int numThreads;

    public FixedThreadPool(int numThreads) {
        super();
        this.numThreads = numThreads;
    }

    @Override
    public void start() {
        for (int i = 0; i < numThreads; i++) {
            WorkerThread worker = new WorkerThread(this);
            workerThreads.add(worker);
            worker.start();
        }
    }

    @Override
    public void execute(Runnable task) {
        synchronized (taskQueue) {
            taskQueue.add(task);
            taskQueue.notify();
        }
    }

    @Override
    public int getCurrentThreadCount() {
        return numThreads;
    }
}

