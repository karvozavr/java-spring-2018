package ru.spbau.mit.karvozavr.threadpool;

import ru.spbau.mit.karvozavr.threadpool.exception.LightExecutionException;

/**
 * Future computation abstraction interface.
 */
public interface LightFuture<T> {

    /**
     * Checks if the computation is ready.
     * @return if the computation is ready
     */
    boolean isReady();

    /**
     * Returns result of the computation, or waits for it to be computed and then returns.
     * @return result of the computation
     * @throws LightExecutionException if computation fails with an exception
     */
    T get() throws LightExecutionException;

    
}
