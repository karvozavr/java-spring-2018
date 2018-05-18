package ru.spbau.mit.karvozavr.my_junit;

import com.sun.istack.internal.NotNull;
import ru.spbau.mit.karvozavr.my_junit.util.TestResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    @NotNull
    public List<TestResult> runTestClass(@NotNull Class<?> testClass) {
        List<TestResult> results = new ArrayList<>();

        for (Method method : testClass.getDeclaredMethods()) {
            Test testAnnotation = method.getAnnotation(Test.class);
            if (testAnnotation != null) {
                results.add(runTestMethod(method));
            }
        }

        return results;
    }

    @NotNull
    public TestResult runTestMethod(@NotNull Method testMethod) {
        Test testAnnotation = testMethod.getAnnotation(Test.class);
        TestResult result;
        if (!testAnnotation.ignore().equals("")) {
            result = new TestResult(testMethod.getName(), TestResult.Result.IGNORED, 0, testAnnotation.ignore());
        } else {
            long start = System.currentTimeMillis();
            try {
                testMethod.setAccessible(true);
                testMethod.invoke(null);
                result = new TestResult(testMethod.getName(), TestResult.Result.SUCCESSFUL, System.currentTimeMillis() - start, null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                result = new TestResult(testMethod.getName(), TestResult.Result.FAILED, System.currentTimeMillis() - start, null);
            }
        }

        return result;
    }
}
