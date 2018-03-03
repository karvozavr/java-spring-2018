package ru.spbau.mit.karvozavr.threadpool;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

public class ThreadPoolImplTest {

    @Test
    public void testSingleTaskExecution() {
        ThreadPoolImpl threadPool = new ThreadPoolImpl(4);
        LightFuture<Integer> value = threadPool.addTask(() -> 42);
        assertThat(value.get(), is(42));
        assertThat(value.isReady(), is(true));
    }

    @Test
    public void testThenApplySmoke() {
        ThreadPoolImpl threadPool = new ThreadPoolImpl(4);
        LightFuture<Integer> value = threadPool.addTask(() -> 42);
        value = value.thenApply(val -> val * val);
        assertThat(value.get(), is(42 * 42));
        assertThat(value.isReady(), is(true));
    }

    @Test
    public void testMultipleTaskExecutionAtOneThread() {
        testTasksExecution(20, 1);
    }

    @Test
    public void testMultipleTaskExecutionAtNormalThreads() {
        testTasksExecution(100, 4);
    }

    @Test
    public void testMultipleTaskExecutionAtManyThreads() {
        testTasksExecution(1000, 100);
    }

    @Test
    public void testNumberOfThreads() throws InterruptedException {
        for (int i = 1; i < 10; i++) {
            ThreadPoolImpl threadPool = new ThreadPoolImpl(i);
            testNumberOfThreadsHelper(threadPool);
            threadPool.shutdown();
        }
    }

    private void testNumberOfThreadsHelper(ThreadPoolImpl threadPool) throws InterruptedException {
        Set<Long> ids = new HashSet<>();
        List<LightFuture<?>> futures = new ArrayList<>();
        for (int i = 0; i < threadPool.getThreadAmount() * 2; i++) {
            futures.add(threadPool.addTask(() -> {
                synchronized (ids) {
                    ids.add(Thread.currentThread().getId());
                }

                while (true) {
                    if (Thread.interrupted()) {
                        break;
                    }
                }

                return null;
            }));
        }

        TimeUnit.MILLISECONDS.sleep(100 * threadPool.getThreadAmount());
        assertThat(ids.size(), is(threadPool.getThreadAmount()));
    }

    /**
     * Parallel map of a function.
     */
    @Test
    public void testMap() {
        ThreadPoolImpl threadPool = new ThreadPoolImpl(10);
        int size = 10000;
        Integer[] array = new Integer[size];
        Integer[] expected = new Integer[size];
        for (int i = 0; i < size; ++i) {
            array[i] = i;
            expected[i] = i;
        }

        for (int i = 0; i < size; i++) {
            expected[i] += 1;
            expected[i] *= expected[i];
            expected[i] *= 42;
        }

        parallelMap(array, threadPool, x -> x + 1, x -> x * x, x -> x * 42);
        assertArrayEquals(expected, array);
        threadPool.shutdown();
    }

    private <T> void parallelMap(final T[] array, ThreadPoolImpl threadPool, Function<T, T>... functions) {
        int threadAmount = threadPool.getThreadAmount();
        int size = (array.length + threadAmount - 1) / threadAmount;

        List<LightFuture<T>> futures = new ArrayList<>();
        for (int i = 0; i < threadAmount; i++) {
            int[] start = {i * size};
            futures.add(threadPool.addTask(() -> {
                int from = start[0];
                int to = Math.min(from + size, array.length);
                for (int j = from; j < to; j++) {
                    array[j] = functions[0].apply(array[j]);
                }
                return null;
            }));
        }

        Arrays.stream(functions)
            .skip(1)
            .forEach(function -> {
                for (int i = 0; i < futures.size(); i++) {
                    int[] start = {i * size};
                    futures.set(i, futures.get(i).thenApply(result -> {
                        int from = start[0];
                        int to = Math.min(from + size, array.length);
                        for (int j = from; j < to; j++) {
                            array[j] = function.apply(array[j]);
                        }
                        return null;
                    }));
                }
            });

        futures.forEach(LightFuture::get);
    }

    private void testTasksExecution(int tasksAmount, int threadsAmount) {
        ThreadPoolImpl threadPool = new ThreadPoolImpl(threadsAmount);
        List<LightFuture<Integer>> tasks = new ArrayList<>();
        for (int i = 0; i < tasksAmount; i++) {
            int[] value = {i};
            tasks.add(threadPool.addTask(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return value[0];
            }));
        }

        for (int i = 0; i < tasksAmount; i++) {
            assertThat(tasks.get(i).get(), is(i));
            assertThat(tasks.get(i).isReady(), is(true));
        }

        threadPool.shutdown();
    }
}