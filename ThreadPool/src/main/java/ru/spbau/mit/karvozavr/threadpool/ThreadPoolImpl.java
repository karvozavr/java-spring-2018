package ru.spbau.mit.karvozavr.threadpool;

import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.karvozavr.threadpool.exception.LightExecutionException;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * ThreadPool implementation with fixed amount of threads.
 */
public class ThreadPoolImpl {

    @NotNull
    private final List<Thread> threads = new ArrayList<>();

    @NotNull
    private final Queue<LightFutureImpl<?>> taskQueue = new LinkedList<>();

    private final int threadAmount;

    /**
     * Constructs ThreadPoolImpl with {@code threadAmount} threads.
     *
     * @param threadAmount amount of threads
     */
    public ThreadPoolImpl(int threadAmount) {
        if (threadAmount < 1) {
            throw new InvalidParameterException("Are you sure, you want to have ThreadPool with " + threadAmount + "threads?");
        }

        for (int i = 0; i < threadAmount; i++) {
            threads.add(new Thread(worker));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        this.threadAmount = threadAmount;
    }

    /**
     * Worker of the single thread in thread pool.
     * It has two states: waiting for task and executing task.
     */
    private Runnable worker = () -> {
        LightFutureImpl<?> computation;

        while (!Thread.interrupted()) {
            try {
                synchronized (taskQueue) {
                    while (taskQueue.isEmpty())
                        taskQueue.wait();
                    computation = taskQueue.poll();
                }
            } catch (InterruptedException e) {
                break;
            }

            computation.run();

            synchronized (computation) {
                computation.notifyAll();
            }
        }
    };

    /**
     * Adds task to thread pool.
     * If there is free thread, the task runs in it, otherwise, it waits for free thread.
     *
     * @param computation task to be added
     * @param <T>         type of computation result
     * @return {@link LightFuture} object, representing this computation
     */
    @NotNull
    public <T> LightFuture<T> addTask(@NotNull Supplier<T> computation) {
        LightFutureImpl<T> task = new LightFutureImpl<>(computation);

        synchronized (taskQueue) {
            taskQueue.add(task);
            taskQueue.notify();
        }

        return task;
    }

    /**
     * Stops all the threads.
     */
    public void shutdown() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    public int getThreadAmount() {
        return threadAmount;
    }

    /**
     * {@link LightFuture} implementation for {@link ThreadPoolImpl}.
     *
     * @param <T> type of future computation
     */
    private class LightFutureImpl<T> implements LightFuture<T> {

        private final Supplier<T> computation;
        private volatile T result;
        private volatile boolean isReady;
        private volatile Throwable exception;


        public LightFutureImpl(@NotNull Supplier<T> computation) {
            this.computation = computation;
        }

        private void run() {
            try {
                result = computation.get();
            } catch (Exception e) {
                exception = e;
            } finally {
                isReady = true;
            }
        }

        @Override
        public boolean isReady() {
            return isReady;
        }

        @Override
        public T get() throws LightExecutionException {
            synchronized (this) {
                while (!isReady) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            if (exception != null)
                throw new LightExecutionException(exception);

            return result;
        }

        @NotNull
        @Override
        public <R> LightFuture<R> thenApply(@NotNull Function<? super T, ? extends R> function) {
            return ThreadPoolImpl.this.addTask(() -> function.apply(get()));
        }
    }
}
