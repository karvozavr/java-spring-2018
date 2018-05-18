package ru.spbau.mit.karvozavr.my_junit.TestClasses;

import ru.spbau.mit.karvozavr.my_junit.annotations.Test;

public class TestClass {

    @Test
    public void fail() throws Exception {
        throw new Exception();
    }

    @Test
    public void success() {

    }

    @Test(ignore = "No need")
    public void ignored() {

    }

    @Test(expected = Exception.class)
    public void expect() throws Exception {
        throw new Exception();
    }
}
