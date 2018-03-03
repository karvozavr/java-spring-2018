package ru.spbau.mit.karvozavr.threadpool.exception;

/**
 * Exception to be thrown on LightFuture execution failure.
 */
public class LightExecutionException extends RuntimeException {

    /**
     * Construct exception from cause.
     * @param cause cause of exception
     */
    public LightExecutionException(Throwable cause) {
        super(cause);
    }
}
