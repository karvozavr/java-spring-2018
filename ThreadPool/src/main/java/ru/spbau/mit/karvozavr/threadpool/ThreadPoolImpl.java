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
    private volatile Queue<LightFutureImpl<?>> tasks = new LinkedList<>();

    /**
     * Worker of the single thread in thread pool.
     * It has two states: waiting for task and executing task.
     */
    private Runnable worker = () -> {
        LightFutureImpl<?> computation;

        outer:
        while (true) {
            if (Thread.interrupted())
                break;

            synchronized (ThreadPoolImpl.this) {
                while (tasks.isEmpty()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        break outer;
                    }
                }

                computation = tasks.peek();
            }

            computation.run();
            computation.notifyAll();
        }
    };

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
    }

    /**
     * Adds task to thread pool.
     * If there is free thread, the tasks runs in it, otherwise, it waits for free thread.
     *
     * @param computation task to be added
     * @param <T>         type of computation result
     * @return {@link LightFuture} object, representing this computation
     */
    @NotNull
    public <T> LightFuture<T> addTask(@NotNull Supplier<T> computation) {
        LightFutureImpl<T> task = new LightFutureImpl<>(computation);

        synchronized (this) {
            tasks.add(task);
            notifyAll();
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


        public LightFutureImpl(Supplier<T> computation) {
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
            while (!isReady) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            if (exception != null)
                throw new LightExecutionException(exception);

            return result;
        }

        @NotNull
        @Override
        public <R> LightFuture<R> thenApply(Function<? super T, ? extends R> function) {
            return ThreadPoolImpl.this.addTask(() -> function.apply(get()));
        }
    }
}
