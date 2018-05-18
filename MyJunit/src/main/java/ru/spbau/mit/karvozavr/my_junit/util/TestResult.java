package ru.spbau.mit.karvozavr.my_junit.util;

public class TestResult {
    public enum Result {
        SUCCESS,
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

    @Override
    public String toString() {
        switch (result) {
            case FAILED:
                return String.format("FAILED \"test\" %s. Duration: %dms. Cause: %s.", testMethodName, duration, message);
            case IGNORED:
                return String.format("IGNORED \"test\" %s. Reason: %s.", testMethodName, message);
            case SUCCESS:
                return String.format("SUCCESS \"test\" %s. Duration: %dms.", testMethodName, duration);
            default:
                return "Incorrect result.";
        }
    }
}
