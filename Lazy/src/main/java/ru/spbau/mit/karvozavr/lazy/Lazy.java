package ru.spbau.mit.karvozavr.lazy;

/**
 * Lazy computation interface.
 * Value is calculated once, when asked, and then cached value is being supplied.
 *
 * @param <T> type of a value
 */
public interface Lazy<T> {

    /**
     * Computes (first time call) or returns cached value.
     *
     * @return value
     */
    T get();
}
