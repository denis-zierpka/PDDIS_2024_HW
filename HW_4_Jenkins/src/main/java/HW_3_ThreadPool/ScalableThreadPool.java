package HW_3_ThreadPool;

public class ScalableThreadPool extends ThreadPoolBasics {
    private final int minThreads;
    private final int maxThreads;
    private int currentThreadCount = 0;

    public ScalableThreadPool(int minThreads, int maxThreads) {
        super();
        this.minThreads = minThreads;
        this.maxThreads = maxThreads;
    }

    @Override
    public void start() {
        for (int i = 0; i < minThreads; i++) {
            addWorkerTry();
        }
    }

    @Override
    public void execute(Runnable task) {
        synchronized (taskQueue) {
            if (currentWorking == currentThreadCount || !taskQueue.isEmpty()) {
                addWorkerTry();
            }
            taskQueue.add(task);
            taskQueue.notifyAll();
        }
    }

    private void addWorkerTry() {
        synchronized (lockThreads) {
            if (currentThreadCount < maxThreads) {
                WorkerThread worker = new WorkerThread(this);
                workerThreads.add(worker);
                worker.start();
                currentThreadCount++;
            }
        }
    }

    @Override
    void optimizeWorkerCount() {
        synchronized (lockThreads) {
            if (!taskQueue.isEmpty() || currentThreadCount <= minThreads) {
                return;
            }

            WorkerThread worker = workerThreads.poll();
            if (worker != null) {
                if (!worker.isWorking()) {
                    worker.shutdown();
                    currentThreadCount--;
                } else {
                    workerThreads.add(worker);
                }
            }
        }
    }

    @Override
    public int getCurrentThreadCount() {
        synchronized (lockThreads) {
            return currentThreadCount;
        }
    }
}
