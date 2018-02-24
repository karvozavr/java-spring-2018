package ru.spbau.mit.karvozavr.lazy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Factory producing {@link Lazy} implementation objects.
 */
public class LazyFactory {

    /**
     * Create single-thread {@link Lazy} implementation (faster).
     *
     * @param supplier supplier of a value
     * @param <T>      type of a value
     * @return single-thread {@link Lazy} implementation supplying value by given supplier
     */
    @NotNull
    public static <T> Lazy<T> createLazy(@NotNull Supplier<T> supplier) {
        return new SingleThreadLazy<>(supplier);
    }

    /**
     * Create concurrent {@link Lazy} implementation.
     *
     * @param supplier supplier of a value
     * @param <T>      type of a value
     * @return concurrent {@code Lazy} implementation supplying value by given supplier
     */
    @NotNull
    public static <T> Lazy<T> createConcurrentLazy(@NotNull Supplier<T> supplier) {
        return new ConcurrentLazy<>(supplier);
    }

    /**
     * Single-thread {@link Lazy} implementation.
     *
     * @param <T> value type
     */
    private static class SingleThreadLazy<T> implements Lazy<T> {

        @Nullable
        private Supplier<T> supplier;
        @Nullable
        private T value;

        /**
         * Constructs {@link SingleThreadLazy} with given supplier.
         *
         * @param supplier supplier of a value
         */
        public SingleThreadLazy(@NotNull Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        @Nullable
        public T get() {
            if (supplier != null) {
                value = supplier.get();
                supplier = null;
            }
            return value;
        }
    }

    /**
     * Concurrent {@link Lazy} implementation.
     *
     * @param <T> value type
     */
    private static class ConcurrentLazy<T> implements Lazy<T> {

        @Nullable
        private volatile Supplier<T> supplier;
        @Nullable
        private volatile T value;

        /**
         * Constructs {@link ConcurrentLazy} with given supplier.
         *
         * @param supplier supplier of a value
         */
        public ConcurrentLazy(@NotNull Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public T get() {
            if (supplier != null) {
                synchronized (this) {
                    if (supplier != null) {
                        value = supplier.get();
                        supplier = null;
                    }
                }
            }

            return value;
        }
    }
}
