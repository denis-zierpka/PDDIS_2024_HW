package HW_3_ThreadPool;

public class WorkerThread extends Thread {
    private final ThreadPoolBasics threadPool;
    private boolean running;
    private boolean working;

    public WorkerThread(ThreadPoolBasics threadPool) {
        this.threadPool = threadPool;
        running = true;
        working = false;
    }

    @Override
    public void run() {
        while (running) {
            Runnable task = threadPool.pollTask();

            if (task != null) {
                working = true;
                threadPool.incrementWorking();
                try {
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace(); // don't block worker
                } finally {
                    working = false;
                    threadPool.decrementWorking();
                    threadPool.releaseSemaphore();
                }
            } else {
                threadPool.optimizeWorkerCount(); // does nothing in FixedThreadPool
            }
        }
    }

    public void shutdown() {
        running = false;
    }

    public boolean isWorking() {
        return working;
    }
}
