package HW_3_ThreadPool;

import java.util.ArrayDeque;
import java.util.Queue;

public class ThreadPoolBasics implements ThreadPool {
    protected final Queue<Runnable> taskQueue;
    protected final Queue<WorkerThread> workerThreads;
    protected final SimpleSemaphore semaphore;
    protected final Object lockThreads = new Object();
    protected int currentWorking = 0;


    protected ThreadPoolBasics() {
        taskQueue = new ArrayDeque<>();
        workerThreads = new ArrayDeque<>();
        semaphore = new SimpleSemaphore(0);
    }

    public void incrementWorking() {
        synchronized (lockThreads) {
            currentWorking++;
        }
    }

    public void decrementWorking() {
        synchronized (lockThreads) {
            currentWorking--;
        }
    }

    public void releaseSemaphore() {
        semaphore.release();
    }

    protected Runnable pollTask() {
        synchronized (taskQueue) {
            return taskQueue.poll();
        }
    }

    public void shutdown() {
        synchronized (lockThreads) {
            for (WorkerThread thread : workerThreads) {
                thread.shutdown();
            }
        }
    }

    public void waitForCompletion(int taskCount) throws InterruptedException {
        for (int i = 0; i < taskCount; i++) {
            semaphore.acquire();
        }
    }

    @Override
    public void start() {}

    @Override
    public void execute(Runnable runnable) {}

    void optimizeWorkerCount() {}

    public int getCurrentThreadCount() {
        return 0;
    }
}
