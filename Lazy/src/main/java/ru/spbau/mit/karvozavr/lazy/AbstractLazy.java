package ru.spbau.mit.karvozavr.lazy;

import com.sun.istack.internal.NotNull;

import java.util.function.Supplier;

public abstract class AbstractLazy<T> implements Lazy<T> {

    @NotNull
    private Supplier<T> supplier;
    protected T value;

    public AbstractLazy(@NotNull Supplier<T> supplier) {
        this.supplier = supplier;
    }

}
