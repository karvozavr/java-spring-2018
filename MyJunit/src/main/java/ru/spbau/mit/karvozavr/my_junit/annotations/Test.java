package ru.spbau.mit.karvozavr.my_junit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Test {
    Class<? extends Throwable> expected() default None.class;

    String ignore() default "";

    class None extends Throwable {
        private None() {
        }
    }
}
