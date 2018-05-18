package ru.spbau.mit.karvozavr.my_junit.util;

import ru.spbau.mit.karvozavr.my_junit.Test;

import java.util.concurrent.TimeUnit;

public class TestResult {
    public enum Result {
        SUCCESSFUL,
        FAILED,
        IGNORED
    }

    public final Result result;
    public final String message;
    public final long duration;
    public final String testMethodName;


    public TestResult(String testMethodName, Result result, long duration, String message) {
        this.testMethodName = testMethodName;
        this.result = result;
        this.duration = duration;
        this.message = message;
    }
}
