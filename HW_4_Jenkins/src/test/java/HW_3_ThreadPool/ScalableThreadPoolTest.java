package HW_3_ThreadPool.Tests;

import HW_3_ThreadPool.ScalableThreadPool;
import HW_3_ThreadPool.ThreadPoolBasics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScalableThreadPoolTest {
    private ThreadPoolBasics threadPool;
    private static int WORKER_COUNT;

    @AfterEach
    void tearDown() {
        threadPool.shutdown();
    }

    @Test
    void testScalableCorrectResults() throws InterruptedException {
        threadPool = new ScalableThreadPool(3, 10);
        WORKER_COUNT = 1000;
        Set<Integer> results = Collections.synchronizedSet(new HashSet<>());
        threadPool.start();

        for (int i = 0; i < WORKER_COUNT; i++) {
            final int taskId = i;
            threadPool.execute(() -> {
                int result = taskId * 2;
                results.add(result);
            });
        }

        threadPool.waitForCompletion(WORKER_COUNT);
        assertEquals(WORKER_COUNT, results.size());
        for (int i = 0; i < WORKER_COUNT; i++) {
            assertTrue(results.contains(i * 2));
        }
    }

    @Test
    void testScalableResultsWithExceptions() throws InterruptedException {
        threadPool = new ScalableThreadPool(3, 10);
        WORKER_COUNT = 20;
        threadPool.start();
        Set<Integer> results = Collections.synchronizedSet(new HashSet<>());
        PrintStream saveErr = saveErr(); // To avoid printing expected errors in test

        for (int i = 0; i < WORKER_COUNT; i++) {
            final int taskId = i;
            threadPool.execute(() -> {
                if (taskId % 2 == 0) {
                    throw new RuntimeException("Custom Expected Exception");
                }
                int result = taskId * 2;
                results.add(result);
            });
        }

        threadPool.waitForCompletion(WORKER_COUNT);
        System.setErr(saveErr);
        assertEquals(WORKER_COUNT / 2, results.size());
        for (int i = 0; i < WORKER_COUNT; i++) {
            if (i % 2 != 0) {
                assertTrue(results.contains(i * 2));
            }
        }
    }

    @Test
    void testScalableUnderLoad() throws InterruptedException {
        threadPool = new ScalableThreadPool(3, 10);
        WORKER_COUNT = 20;
        threadPool.start();

        for (int i = 0; i < WORKER_COUNT; i++) {
            int smallShift = i;
            threadPool.execute(() -> {
                try {
                    Thread.sleep(1000 + smallShift);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        assertEquals(10, threadPool.getCurrentThreadCount());

        long startTime = System.currentTimeMillis();
        threadPool.waitForCompletion(WORKER_COUNT);
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertTrue(elapsedTime >= 1800 && elapsedTime <= 2200);

        Thread.sleep(100);
        assertEquals(3, threadPool.getCurrentThreadCount());
    }

    @Test
    void testScalableUnderLoadWithExceptions() throws InterruptedException {
        threadPool = new ScalableThreadPool(3, 10);
        WORKER_COUNT = 20;
        threadPool.start();
        PrintStream saveErr = saveErr(); // To avoid printing expected errors in test

        for (int i = 0; i < WORKER_COUNT; i++) {
            int smallShift = i;
            threadPool.execute(() -> {
                if (smallShift % 2 == 0) {
                    throw new RuntimeException("Custom Expected Exception");
                }
                try {
                    Thread.sleep(1000 + smallShift);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        long startTime = System.currentTimeMillis();
        threadPool.waitForCompletion(WORKER_COUNT);
        System.setErr(saveErr);
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertTrue(elapsedTime >= 800 && elapsedTime <= 1200);
    }

    PrintStream saveErr() {
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {}
        }));

        return originalErr;
    }
}


