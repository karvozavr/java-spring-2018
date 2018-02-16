package ru.spbau.mit.karvozavr.lazy;

import com.sun.istack.internal.NotNull;

import java.util.function.Supplier;

public class LazyFactory {

    public static <T> Lazy<T> createLazy(@NotNull Supplier<T> supplier) {
        return new AbstractLazy<T>(supplier) {
            @Override
            public T get() {
                if (supplier == null)
                    return value;
                else
                    return supplier.get();
            }
        };
    }

    public static <T> Lazy<T> createConcurrentLazy(@NotNull Supplier<T> supplier) {
        return null;
    }

    private class ConcurrentLazy<T> implements Lazy<T> {

        private volatile Supplier<T> supplier;
        private T value = (T) new Object();

        public ConcurrentLazy(@NotNull Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public T get() {
            if (supplier != null) {
                synchronized (value) {
                    value = supplier.get();
                    supplier = null;
                }
            }

            return value;
        }
    }
}
