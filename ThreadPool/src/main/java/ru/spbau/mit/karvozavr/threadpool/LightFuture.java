package ru.spbau.mit.karvozavr.threadpool;

import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.karvozavr.threadpool.exception.LightExecutionException;

import java.util.function.Function;

/**
 * Future computation abstraction interface.
 */
public interface LightFuture<T> {

    /**
     * Checks if the computation is ready.
     *
     * @return if the computation is ready
     */
    boolean isReady();

    /**
     * Returns result of the computation, or waits for it to be computed and then returns.
     *
     * @return result of the computation
     * @throws LightExecutionException if computation fails with an exception
     */
    T get() throws LightExecutionException;

    /**
     * Returns new {@link LightFuture} computation, that applies {@code function} to result of computation
     *
     * @param function function to process computation's result
     * @param <R>      return value of a function
     * @return new {@link LightFuture} computation, that applies {@code function} to result of computation
     */
    @NotNull <R> LightFuture<R> thenApply(Function<? super T, ? extends R> function);
}
