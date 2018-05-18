package ru.spbau.mit.karvozavr.my_junit;


import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.karvozavr.my_junit.annotations.*;
import ru.spbau.mit.karvozavr.my_junit.util.TestResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    private List<Method> before = new ArrayList<>();
    private List<Method> after = new ArrayList<>();
    private List<Method> beforeClass = new ArrayList<>();
    private List<Method> afterClass = new ArrayList<>();
    private Object testClassInstance;

    public TestRunner(Class<?> testClass) throws IllegalAccessException, InstantiationException {
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAccessible()) {
                Before beforeAnnotation = method.getAnnotation(Before.class);
                if (beforeAnnotation != null) {
                    before.add(method);
                }

                After afterAnnotation = method.getAnnotation(After.class);
                if (afterAnnotation != null) {
                    after.add(method);
                }

                BeforeClass beforeClassAnnotation = method.getAnnotation(BeforeClass.class);
                if (beforeClassAnnotation != null) {
                    beforeClass.add(method);
                }

                AfterClass afterClassAnnotation = method.getAnnotation(AfterClass.class);
                if (afterClassAnnotation != null) {
                    afterClass.add(method);
                }
            }
        }

        this.testClassInstance = testClass.newInstance();
    }

    @NotNull
    public List<TestResult> runTestClass() throws InvocationTargetException, IllegalAccessException {
        List<TestResult> results = new ArrayList<>();
        beforeClass();

        for (Method method : testClassInstance.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.isAccessible()) {
                Test testAnnotation = method.getAnnotation(Test.class);
                if (testAnnotation != null) {
                    results.add(runTestMethod(method));
                }
            }
        }

        afterClass();
        return results;
    }

    @NotNull
    private TestResult runTestMethod(@NotNull Method testMethod) throws InvocationTargetException, IllegalAccessException {
        before();
        Test testAnnotation = testMethod.getAnnotation(Test.class);
        TestResult result;

        if (!testAnnotation.ignore().equals("")) {
            result = new TestResult(testMethod.getName(), TestResult.Result.IGNORED, 0, testAnnotation.ignore());
        } else {
            long start = System.currentTimeMillis();
            try {
                testMethod.setAccessible(true);
                testMethod.invoke(testClassInstance);
                result = new TestResult(testMethod.getName(), TestResult.Result.SUCCESS, System.currentTimeMillis() - start, null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                if (e.getCause().getClass().equals(testAnnotation.expected()))
                    result = new TestResult(testMethod.getName(), TestResult.Result.SUCCESS, System.currentTimeMillis() - start, null);
                else
                    result = new TestResult(testMethod.getName(), TestResult.Result.FAILED, System.currentTimeMillis() - start, e.getCause().toString());
            }
        }

        after();
        return result;
    }

    private void before() throws InvocationTargetException, IllegalAccessException {
        for (Method method : before) {
            method.invoke(testClassInstance);
        }
    }

    private void after() throws InvocationTargetException, IllegalAccessException {
        for (Method method : after) {
            method.invoke(testClassInstance);
        }
    }

    private void beforeClass() throws InvocationTargetException, IllegalAccessException {
        for (Method method : beforeClass) {
            method.invoke(testClassInstance);
        }
    }

    private void afterClass() throws InvocationTargetException, IllegalAccessException {
        for (Method method : afterClass) {
            method.invoke(testClassInstance);
        }
    }
}
