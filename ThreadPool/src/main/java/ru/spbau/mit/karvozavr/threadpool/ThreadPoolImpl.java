package ru.spbau.mit.karvozavr.threadpool;

import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * ThreadPool implementation with fixed amount of threads.
 */
public class ThreadPoolImpl {

    @NotNull
    private final List<Thread> threads = new ArrayList<>();

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
            threads.add(new Thread());
        }
    }

    @NotNull
    public <T> LightFuture<T> addTask(@NotNull Supplier<T> computation) {
        return null;
    }
}
