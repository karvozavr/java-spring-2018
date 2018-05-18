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
    public void testIgnored() throws InvocationTargetException, IllegalAccessException {
        List<TestResult> results= testRunner.runTestClass();
        TestResult result = results.get(3);
        assertThat(result.testMethodName, is("ignored"));
        assertThat(result.result, is(TestResult.Result.IGNORED));
        assertThat(result.message, is("No need"));
    }

    @Test
    public void testSuccessful() throws InvocationTargetException, IllegalAccessException {
        List<TestResult> results= testRunner.runTestClass();
        TestResult result = results.get(2);
        assertThat(result.testMethodName, is("success"));
        assertThat(result.result, is(TestResult.Result.SUCCESS));
    }

    @Test
    public void testFailed() throws InvocationTargetException, IllegalAccessException {
        List<TestResult> results= testRunner.runTestClass();
        TestResult result = results.get(0);
        assertThat(result.testMethodName, is("fail"));
        assertThat(result.result, is(TestResult.Result.FAILED));
    }

    @Test
    public void testExpect() throws InvocationTargetException, IllegalAccessException {
        List<TestResult> results= testRunner.runTestClass();
        TestResult result = results.get(1);
        assertThat(result.testMethodName, is("expect"));
        assertThat(result.result, is(TestResult.Result.SUCCESS));
    }
}