package ru.spbau.mit.karvozavr.my_junit;

import ru.spbau.mit.karvozavr.my_junit.util.TestResult;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Main application class.
 */
public class Main {

    /**
     * Run tests in given jar.
     *
     * @param jarPath path to jar
     * @throws Exception in case of error
     */
    private static void runTests(String jarPath) throws Exception {
        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> entries = jarFile.entries();

        URL[] urls = {new URL("jar:file:" + jarPath + "!/")};
        URLClassLoader classLoader = URLClassLoader.newInstance(urls);

        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                continue;
            }
            String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6);
            className = className.replace('/', '.');
            Class aClass = classLoader.loadClass(className);
            TestRunner testRunner = new TestRunner(aClass);
            List<TestResult> results = testRunner.runTestClass();
            results.forEach(result -> System.out.println(result.toString()));
        }
    }

    public static void main(String[] args) {
        if (args.length >= 1) {
            try {
                runTests(args[0]);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Something went wrong.");
            }
        } else {
            System.err.println("Provide directory.");
        }
    }
}
