package ru.spbau.mit.karvozavr.lazy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;

public class LazyFactoryTest {

    @Test
    public void testConcurrentLazyShort() throws InterruptedException {
        TestWorkerShort workerShort = new TestWorkerShort();
        Lazy<Integer> lazy = LazyFactory.createConcurrentLazy(workerShort);

        runThreadsWithCheck(1000, lazy::get, (threads) -> {});
        assertThat("Computed too many times.", workerShort.isCorrect());
    }

    @Test
    public void testConcurrentThreadLazyLong() throws InterruptedException {
        TestWorkerLong workerLong = new TestWorkerLong();
        Lazy<Integer> lazy = LazyFactory.createConcurrentLazy(workerLong);

        runThreadsWithCheck(1000, lazy::get, (threads) -> {
        });
        assertThat("Computed too many times.", workerLong.isCorrect());
    }

    @Test
    public void testSingleThreadLazyLong() {
        TestWorkerLong workerLong = new TestWorkerLong();
        Lazy<Integer> lazy = LazyFactory.createLazy(workerLong);

        for (int i = 0; i < 100; i++) {
            lazy.get();
        }

        assertThat("Computed too many times.", workerLong.isCorrect());
    }

    @Test
    public void testSingleThreadLazyShort() {
        TestWorkerShort workerShort = new TestWorkerShort();
        Lazy<Integer> lazy = LazyFactory.createLazy(workerShort);

        for (int i = 0; i < 100; i++) {
            lazy.get();
        }

        assertThat("Computed too many times.", workerShort.isCorrect());
    }


    private void runThreadsWithCheck(int amount, Runnable runnable, Consumer<List<Thread>> checker) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            threads.add(new Thread(runnable));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        checker.accept(threads);
    }

    private static abstract class AbstractTestWorker implements Supplier<Integer> {

        protected int timesCalled = 0;

        public boolean isCorrect() {
            return timesCalled == 1;
        }
    }

    private static class TestWorkerShort extends AbstractTestWorker {

        @Override
        public Integer get() {
            ++timesCalled;
            return 42;
        }
    }

    private static class TestWorkerLong extends AbstractTestWorker {

        @Override
        public Integer get() {
            ++timesCalled;

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException("Do not interrupt me!");
            }

            return 42;
        }
    }
}