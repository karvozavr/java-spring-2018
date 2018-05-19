package ru.spbau.mit.karvozavr.my_junit;

import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.karvozavr.my_junit.TestClasses.TestClass;
import ru.spbau.mit.karvozavr.my_junit.util.TestResult;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class TestRunnerTest {

    TestRunner testRunner;

    @Before
    public void before() throws InstantiationException, IllegalAccessException {
        testRunner = new TestRunner(TestClass.class);
    }

    @Test
    public void testAll() throws InvocationTargetException, IllegalAccessException {
        List<TestResult> results = testRunner.runTestClass();
        assertThat(results.size(), is(4));
    }

    @Test
    public void testIgnored() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        TestResult result = testRunner.runTestMethod(TestClass.class.getMethod("ignored"));
        assertThat(result.testMethodName, is("ignored"));
        assertThat(result.result, is(TestResult.Result.IGNORED));
        assertThat(result.message, is("No need"));
    }

    @Test
    public void testSuccessful() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        TestResult result = testRunner.runTestMethod(TestClass.class.getMethod("success"));
        assertThat(result.testMethodName, is("success"));
        assertThat(result.result, is(TestResult.Result.SUCCESS));
    }

    @Test
    public void testFailed() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        TestResult result = testRunner.runTestMethod(TestClass.class.getMethod("fail"));
        assertThat(result.testMethodName, is("fail"));
        assertThat(result.result, is(TestResult.Result.FAILED));
    }

    @Test
    public void testExpect() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        TestResult result = testRunner.runTestMethod(TestClass.class.getMethod("expect"));
        assertThat(result.testMethodName, is("expect"));
        assertThat(result.result, is(TestResult.Result.SUCCESS));
    }
}