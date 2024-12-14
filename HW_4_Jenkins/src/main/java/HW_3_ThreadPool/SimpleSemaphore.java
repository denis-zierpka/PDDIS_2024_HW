package HW_3_ThreadPool;

class SimpleSemaphore {
    private int permits;

    SimpleSemaphore(int initialPermits) {
        this.permits = initialPermits;
    }

    public synchronized void acquire() throws InterruptedException {
        while (permits == 0) {
            wait();
        }
        permits--;
    }

    public synchronized void release() {
        permits++;
        notify();
    }
}
